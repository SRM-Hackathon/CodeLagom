
from flask import  render_template, url_for, flash, redirect ,request ,send_from_directory
from f1 import app, db, bcrypt
from f1.forms import RegistrationForm, LoginForm
from f1.models import User
from flask_login import login_user, current_user, logout_user, login_required
import os
import PyPDF2
from os import chdir, getcwd, listdir, path

import serial
import time


APP_ROOT = os.path.dirname(os.path.abspath(__file__))

@app.route("/")
@app.route("/home")
def home():
    return render_template('home.html')


@app.route("/contact")
def contact():
    return render_template('contact.html', title='Contact')


@app.route("/register", methods=['GET', 'POST'])
def register():
    if current_user.is_authenticated:
        return redirect(url_for('home'))
    form = RegistrationForm()
    db.create_all()
    if form.validate_on_submit():
        hashed_password=bcrypt.generate_password_hash(form.password.data).decode('utf-8')
        user = User(username=form.username.data, email=form.email.data, password=hashed_password)
        db.session.add(user)
        db.session.commit()
        flash(f'Account created! You can now login to your account.', 'success')
        return redirect(url_for('login'))
    return render_template('register.html', title='Register', form=form)


@app.route("/login", methods=['GET', 'POST'])
def login():
    if current_user.is_authenticated:
        return redirect(url_for('home'))
    form = LoginForm()
    if form.validate_on_submit():
        user=User.query.filter_by(email=form.email.data).first()
        if user and bcrypt.check_password_hash(user.password,form.password.data):
            login_user(user,remember=form.remember.data)
            return redirect(url_for('upload'))

        else:
            flash('login unsuccessful.please check again', 'danger')
    return render_template('login.html', title='Login', form=form)

@app.route("/upload", methods=['GET','POST'])
@login_required
def upload():
    if request.method == 'POST':
            target = os.path.join(APP_ROOT, current_user.username)
            print(target)
            if not os.path.isdir(target):
                os.mkdir(target)
            else:
               print("Couldn't create upload directory: {}".format(target))
            print(request.files.getlist("file"))
            for upload in request.files.getlist("file"):
                print(upload)
                print("{} is the file name".format(upload.filename))
                filename = upload.filename
                destination = "/".join([target, filename])
                print("Accept incoming file:", filename)
                print("Save it to:", destination)
                upload.save(destination)
    return render_template('upload.html')

@app.route("/pdftotext1")
def pdftotext1():
    return render_template('pdftotext.html')

@app.route("/pdftotext", methods=["POST"])
def pdftotext():
    if request.method == 'POST':
        folder = os.path.join(APP_ROOT, current_user.username)
        list = []
        directory = folder
        for root, dirs, files in os.walk(directory):
            for filename in files:
                if filename.endswith('.pdf'):
                    t = os.path.join(directory,filename)
                    list.append(t)
        for item in list:
            path = item
            head, tail = os.path.split(path)
            var = "\\"
            tail = tail.replace(".pdf", ".txt")
            name = head + var + tail
            content = ""
            pdf = PyPDF2.PdfFileReader(path, "rb")
            for i in range(0, pdf.getNumPages()):
                content += pdf.getPage(i).extractText() + "\n"
            with open(name, 'a') as out:
                out.write(content)
                out.close()
    return redirect("allfiles")

