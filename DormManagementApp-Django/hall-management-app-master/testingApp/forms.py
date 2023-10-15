from decimal import Decimal

from django import forms
from django.contrib.auth.models import User
from django.contrib.auth.forms import UserCreationForm, AuthenticationForm
from django.core.validators import MinValueValidator

from .models import Profile, Complaint, Student


class RegistrationForm(UserCreationForm):
    role = forms.ChoiceField(choices=Profile.ROLES)

    class Meta:
        model = User
        fields = ('username', 'password1', 'password2', 'role')

    def __init__(self, *args, **kwargs):
        super(RegistrationForm, self).__init__(*args, **kwargs)
        for field_name, field in self.fields.items():
            if field.widget.attrs.get('class'):
                field.widget.attrs['class'] += ' form-control'
            else:
                field.widget.attrs['class'] = 'form-control'
            if field.widget.attrs.get('placeholder'):
                pass
            else:
                field.widget.attrs['placeholder'] = field_name
        self.fields['password1'].widget.attrs['placeholder'] = 'Password'
        self.fields['password2'].widget.attrs['placeholder'] = 'Confirm password'
        self.fields['username'].widget.attrs['placeholder'] = 'Stakeholder ID (e.g. 20CS10081)'

    def save(self, commit=True):
        if not commit:
            raise NotImplementedError("Error")
        user = super(RegistrationForm, self).save(commit=True)
        user_profile = Profile(user=user, role=self.cleaned_data['role'])
        user_profile.save()
        if user_profile.role == 'student':
            user_student = Student(student=user, hall=None, mess_fees=0, rent_amount=0, surcharges=0)
            user_student.save()
            return user, user_profile, user_student
        else:
            return user, user_profile


class LoginForm(AuthenticationForm):
    def __init__(self, *args, **kwargs):
        super(LoginForm, self).__init__(*args, **kwargs)
        self.fields['username'].widget.attrs['class'] = 'form-control'
        self.fields['username'].widget.attrs['placeholder'] = 'Stakeholder ID (e.g. 20CS10081)'

        self.fields['password'].widget.attrs['class'] = 'form-control'
        self.fields['password'].widget.attrs['placeholder'] = 'Password'


class ComplaintForm(forms.ModelForm):
    class Meta:
        model = Complaint
        fields = ['complaint_type', 'description', 'image_upload']

    def __init__(self, *args, **kwargs):
        super(ComplaintForm, self).__init__(*args, **kwargs)
        self.fields['description'].widget.attrs['placeholder'] = 'Describe your problem'
        for field_name in self.fields:
            self.fields[field_name].widget.attrs['class'] = 'form-control'


class ATRUploadForm(forms.Form):
    action_taken_report = forms.FileField()

    def __self__(self, *args, **kwargs):
        super(ATRUploadForm, self).__init__(*args, **kwargs)
        self.fields['action_taken_report'].widget.attrs['class'] = 'form-control'


class ChargeFeesForm(forms.Form):
    rentCap1 = forms.DecimalField(max_digits=10, decimal_places=2, validators=[MinValueValidator(Decimal('0.01'))],
                                  widget=forms.NumberInput(
                                      attrs={'class': 'form-control', 'placeholder': 'Capacity 1 Rent'}))
    rentCap2 = forms.DecimalField(max_digits=10, decimal_places=2, validators=[MinValueValidator(Decimal('0.01'))],
                                  widget=forms.NumberInput(
                                      attrs={'class': 'form-control', 'placeholder': 'Capacity 2 Rent'}))
    rentCap3 = forms.DecimalField(max_digits=10, decimal_places=2, validators=[MinValueValidator(Decimal('0.01'))],
                                  widget=forms.NumberInput(
                                      attrs={'class': 'form-control', 'placeholder': 'Capacity 3 Rent'}))
    rentCap4 = forms.DecimalField(max_digits=10, decimal_places=2, validators=[MinValueValidator(Decimal('0.01'))],
                                  widget=forms.NumberInput(
                                      attrs={'class': 'form-control', 'placeholder': 'Capacity 4 Rent'}))
    surcharges = forms.DecimalField(max_digits=10, decimal_places=2, validators=[MinValueValidator(Decimal('0.01'))],
                                    widget=forms.NumberInput(
                                        attrs={'class': 'form-control', 'placeholder': 'Surcharges'}))

    def __self__(self, *args, **kwargs):
        super(ChargeFeesForm, self).__init__(*args, **kwargs)


class MessFeesForm(forms.Form):
    student = forms.CharField(max_length=150)
    mess_fees = forms.DecimalField(max_digits=10, decimal_places=2, validators=[MinValueValidator(Decimal('0.01'))])

    def __self__(self, *args, **kwargs):
        super(ChargeFeesForm, self).__init__(*args, **kwargs)
        self.fields['student'].widget.attrs['class'] = 'form-control'
        self.fields['student'].widget.attrs['placeholder'] = 'Student Roll Number'

        self.fields['mess_fees'].widget.attrs['class'] = 'form-control'
        self.fields['mess_fees'].widget.attrs['placeholder'] = 'Mess Fees'
