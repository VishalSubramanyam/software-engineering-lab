from django.db.models import F
from django.shortcuts import render, redirect
from django.contrib import messages
from django.contrib.auth.decorators import login_required

from .forms import *
from .models import *
import datetime

today = datetime.date.today()


# Create your views here.
def register(request):
    if request.method == 'POST':
        # given the POST data, is_valid detects any errors
        # If there are errors, the condition fails, and the registration
        # form is displayed again with the errors marked.
        form = RegistrationForm(request.POST)
        if form.is_valid():
            username = form.cleaned_data.get('username')
            form.save()
            messages.success(request, f'Registration successful for {username}')
            return redirect('registration-page')
    else:
        form = RegistrationForm()
    return render(request, 'users/registration.html', {'form': form})


@login_required
def dashboard(request):
    current_user = request.user
    return render(request, f'users/dashboard_{current_user.profile.role}.html', {})


@login_required
def file_complaint(request):
    if request.user.profile.role != 'student':
        return redirect('access-denied')
    elif request.method == 'POST':
        form = ComplaintForm(request.POST, request.FILES)
        if form.is_valid():
            complaint = form.save(commit=False)
            complaint.complainant = request.user
            complaint.save()
            messages.success(request, f'Complaint successfully filed!')
            return redirect('file-complaint')
    else:
        form = ComplaintForm()
    return render(request, 'file-complaint.html', {'form': form})


@login_required
def view_complaints(request):
    if request.method == 'GET':
        if request.user.profile.role == 'student':
            complaints = Complaint.objects.filter(complainant=request.user)
            return render(request, 'view-complaints.html',
                          {'complaints': complaints, 'role': request.user.profile.role})
        elif request.user.profile.role == 'warden':
            studentsInThisHall = User.objects.filter(student__hall=request.user.hall)
            relevantComplaints = Complaint.objects.filter(complainant__in=studentsInThisHall)

            atr_form = ATRUploadForm()
            return render(request, 'view-complaints.html',
                          {'complaints': relevantComplaints, 'role': request.user.profile.role, 'atr_form': atr_form})
        elif request.user.profile.role == 'mess_manager':
            relevantHalls = Hall.objects.filter(mess_manager=request.user)
            relevantComplaints = Complaint.objects.filter(complaint_type='mess').filter(
                complainant__student__hall__in=relevantHalls)
            return render(request, 'view-complaints.html', {'complaints': relevantComplaints, 'role': 'mess_manager'})
        else:
            return redirect('access-denied')


@login_required
def file_atr(request):
    if request.method == 'POST' and request.user.profile.role == 'warden':
        form = ATRUploadForm(request.POST, request.FILES)
        if form.is_valid():
            current_complaint = Complaint.objects.get(pk=request.POST['complaint_id'])
            current_complaint.action_taken_report = form.cleaned_data['action_taken_report']
            current_complaint.save(update_fields=['action_taken_report'])
            messages.success(request, 'Report filed successfully!')
            return redirect('view-complaints')
        else:
            messages.error(request, 'Something went wrong. Try again later.')
            return redirect('view-complaints')

    else:
        return redirect('access-denied')


@login_required
def hall_fees(request):
    if request.method == 'POST':
        if request.user.profile.role == 'student':
            cur_student = request.user.student
            cur_student.hall.running_account += cur_student.rent_amount + cur_student.surcharges
            cur_student.rent_amount = 0
            cur_student.surcharges = 0
            cur_student.save(update_fields=['rent_amount', 'surcharges'])
            cur_student.hall.save(update_fields=['running_account'])
            messages.success(request, 'Hall fees paid successfully!')
            return redirect('hall-fees')
        elif request.user.profile.role == 'warden':
            form = ChargeFeesForm(request.POST)
            if form.is_valid():
                cap1Students = Student.objects.filter(hall=request.user.hall).filter(room__capacity=1)
                cap2Students = Student.objects.filter(hall=request.user.hall).filter(room__capacity=2)
                cap3Students = Student.objects.filter(hall=request.user.hall).filter(room__capacity=3)
                cap4Students = Student.objects.filter(hall=request.user.hall).filter(room__capacity=4)
                cap1Students.update(rent_amount=F('rent_amount') + form.cleaned_data['rentCap1'])
                cap2Students.update(rent_amount=F('rent_amount') + form.cleaned_data['rentCap2'])
                cap3Students.update(rent_amount=F('rent_amount') + form.cleaned_data['rentCap3'])
                cap4Students.update(rent_amount=F('rent_amount') + form.cleaned_data['rentCap4'])
                Student.objects.filter(hall=request.user.hall).update(
                    surcharges=F('surcharges') + form.cleaned_data['surcharges'])
                messages.success(request, 'Rent and surcharges have been successfully demanded')
                return redirect('hall-fees')
            else:
                messages.error(request, 'Something went wrong. Try again later!')
                return redirect('hall-fees')
        else:
            return redirect('access-denied')

    elif request.method == 'GET':
        if request.user.profile.role == 'student':
            rent_owed = request.user.student.rent_amount
            surcharges = request.user.student.surcharges
            return render(request, 'fees-dues/hall-fees.html', {'rent_owed': rent_owed, 'surcharges_owed': surcharges,
                                                                'total_money_owed': rent_owed + surcharges})
        elif request.user.profile.role == 'warden':
            form = ChargeFeesForm()
            return render(request, 'fees-dues/hall-fees-warden.html', {'form': form})
        else:
            return redirect('access-denied')


@login_required
def mess_dues(request):
    cur_user = request.user
    if request.method == 'GET':
        match cur_user.profile.role:
            case 'warden':
                return render(request, 'fees-dues/mess-dues-warden.html', {'mess_dues': cur_user.hall.mess_due})
            case 'student':
                return render(request, 'fees-dues/mess-dues.html', {'mess_dues': cur_user.student.mess_fees})
            case 'mess_manager':
                form = MessFeesForm()
                return render(request, 'fees-dues/mess-dues-manager.html', {'form': form})
            case default:
                return render(request, 'users/access_denied.html')
    elif request.method == 'POST':
        match cur_user.profile.role:
            case 'warden':
                cur_user.hall.running_account -= cur_user.hall.mess_dues
                cur_user.hall.mess_dues = 0
            case 'student':
                # money is paid to the hall, which in turn pays the mess manager
                cur_user.student.hall.running_account += cur_user.student.mess_fees
                cur_user.student.mess_fees = 0  # has paid the fees
                cur_user.student.hall.save(update_fields=['running_account'])  # Update database
                cur_user.student.save(update_fields=['mess_fees'])
                messages.success(request, 'Paid successfully!')
                return redirect('mess-dues')
            case 'mess_manager':
                form = MessFeesForm(request.POST)
                if form.is_valid():
                    cur_student = Student.objects.get(student__username=form.cleaned_data['student'])
                    if not cur_student:
                        messages.error(request, "No such student exists")
                        return redirect('mess-dues')
                    if cur_student.hall.mess_manager != request.user:
                        messages.error(request, "This student isn't served by you")
                        return redirect('mess-dues')
                    cur_student.mess_fees += form.cleaned_data['mess_fees']

                    # this money is owed to the mess manager by the hall.
                    # the hall warden should collect the money from the student to balance the books.
                    cur_student.hall.mess_dues += form.cleaned_data['mess_fees']
                    cur_student.save(update_fields=['mess_fees'])

                    messages.success(request, 'Mess fees demanded successfully!')
                    return redirect('mess-dues')
                else:
                    messages.error(request, 'Invalid input. Try again.')
                    return redirect('mess-dues')
            case default:
                return render(request, 'users/access_denied.html')


@login_required
def expense_report(request):
    if request.user.profile.role != 'warden':
        return render(request, 'users/access_denied.html')
    else:
        return render(request, 'fees-dues/hall-expense-report.html',
                      {'mess_dues': request.user.hall.mess_dues, 'running_account': request.user.hall.running_account})


@login_required
def petty_expenses(request):
    if request.user.profile.role != 'admin':
        return redirect('access-denied')
    else:
        RelevantExpenses = HMCPettyExpense.objects.filter(date_of_occurrence__month=today.month)
        sum = 0
        for expense in RelevantExpenses:
            sum += expense.cost
        return render(request, 'petty-expenses.html', {'expenses': RelevantExpenses, 'sum': sum})


@login_required
def view_hmc_employees(request):
    if request.user.profile.role != 'admin':
        return redirect('access-denied')
    else:
        employees = HMCEmployee.objects.all()
        sum = 0
        for employee in employees:
            sum += employee.monthly_salary
        return render(request, 'view-hmc-employees.html', {'employees': employees, 'sum': sum})


def under_construction(request):
    return render(request, 'under_construction.html')


def access_denied(request):
    return render(request, 'users/access_denied.html')
