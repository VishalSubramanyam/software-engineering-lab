from django.db import models
from django.contrib.auth.models import User
import os
import uuid


# Create your models here.
class Profile(models.Model):
    user = models.OneToOneField(User, on_delete=models.CASCADE)

    ROLES = [
        ('student', 'Student'),
        ('warden', 'Warden'),
        ('hall_clerk', 'Hall Clerk'),
        ('hmc_chairman', 'HMC Chairman'),
        ('mess_manager', 'Mess Manager'),
        ('admin', 'Administrator')
    ]
    role = models.CharField(
        max_length=30,
        choices=ROLES,
        default='student'
    )

    def __str__(self):
        return str(self.user.username)


class Hall(models.Model):
    hall_name = models.CharField(max_length=100)
    warden = models.OneToOneField(User, null=True,
                                  on_delete=models.SET_NULL)  # can delete wardens without affecting Halls
    mess_manager = models.ForeignKey(User, null=True, blank=True, on_delete=models.SET_NULL, related_name='+')
    grant_received = models.DecimalField(max_digits=10, default=0, decimal_places=2)
    daily_wage_expenses = models.DecimalField(max_digits=10, default=0, decimal_places=2)
    running_account = models.DecimalField(max_digits=10, default=0, decimal_places=2)
    mess_dues = models.DecimalField(max_digits=10, default=0, decimal_places=2)

    def __str__(self):
        return self.hall_name.__str__()


class Room(models.Model):
    room_id = models.CharField(max_length=10)  # For example, SDS431 is an identifier for a room in MMM.
    CAPACITIES = [
        (1, 'One'),
        (2, 'Two'),
        (3, 'Three'),
        (4, 'Four')
    ]
    capacity = models.IntegerField(choices=CAPACITIES, default=2)
    # Each room is inside a hall
    # Each hall has many rooms. So this forms a MANY-ONE relationship, hence a foreign key has been used
    hall = models.ForeignKey(Hall, null=False, on_delete=models.CASCADE)

    def __str__(self):
        return str(self.room_id)


class Student(models.Model):
    # student information
    student = models.OneToOneField(User, on_delete=models.CASCADE)  # each student "IS-A" user
    hall = models.ForeignKey(Hall, null=True, on_delete=models.SET_NULL)
    room = models.ForeignKey(Room, null=True, blank=True, on_delete=models.SET_NULL)

    # Accounting columns
    mess_fees = models.DecimalField(max_digits=10, decimal_places=2)
    rent_amount = models.DecimalField(max_digits=10, decimal_places=2)
    surcharges = models.DecimalField(max_digits=10, decimal_places=2)

    def __str__(self):
        return str(self.student.username)


def _get_image_upload_path(instance, filename):
    ext = filename.split('.')[-1]
    filename = "%s.%s" % (uuid.uuid4(), ext)
    return os.path.join('complaints', filename)


def _get_atr_upload_path(instance, filename):
    ext = filename.split('.')[-1]
    filename = "%s.%s" % (uuid.uuid4(), ext)
    return os.path.join('action_taken_reports', filename)


class Complaint(models.Model):
    date_filed = models.DateField(auto_now_add=True)
    COMP_TYPES = [
        ('mess', 'Mess related issues'),
        ('electrical', 'Electrical issues'),
        ('cleaning', 'Cleaning'),
        ('other', 'Other')
    ]
    complaint_type = models.CharField(max_length=20, choices=COMP_TYPES, default='other')
    complainant = models.ForeignKey(User,
                                    on_delete=models.CASCADE)  # if student is deleted, complaint is also deleted
    description = models.TextField(null=False, blank=False)

    image_upload = models.ImageField(null=True, blank=True, upload_to=_get_image_upload_path)
    action_taken_report = models.FileField(null=True, blank=True, upload_to=_get_atr_upload_path)


class HMCEmployee(models.Model):
    name = models.CharField(max_length=100)
    position = models.CharField(max_length=100)
    dob = models.DateField()
    email = models.EmailField()
    phone = models.PositiveIntegerField(max_length=15)
    address = models.CharField(max_length=300)
    monthly_salary = models.DecimalField(max_digits=10, decimal_places=2)

    def __str__(self):
        return self.name


# only administrators can add these expenses.
# it is assumed that admins are working under the HMC
class HMCPettyExpense(models.Model):
    description = models.CharField(max_length=50)
    cost = models.DecimalField(max_digits=10, decimal_places=2)
    date_of_occurrence = models.DateField(auto_now_add=True)
