Dependencies: Python >3.10, Django, Pillow

Instructions:
1. After installing the dependencies, run the following commands:
    a) 'python manage.py makemigrations'
    b) 'python manage.py migrate'
    c) 'python manage.py runserver'
2. The server must now be running at localhost:8000 (unless there's a blocking service)
3. Open "localhost:8000/testingApp" on your browser
4. Test the program as needed


Procedure to create an administrator:
1. Create an administrator via 'python manage.py createsuperuser'
2. Associate this administrator with the administrator profile using the admin page available at localhost:8000/admin