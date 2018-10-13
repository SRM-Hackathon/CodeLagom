
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

@app.route("/logout")
def logout():
    logout_user()
    return redirect(url_for('home'))



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





@app.route("/test" , methods=['GET', 'POST'])
def test():
    if request.method =='POST':
        target1= os.path.join(APP_ROOT, current_user.username)
        selected_pdf=request.form.get('comp_select')
        target2 = os.path.join(target1, selected_pdf)
        bluetooth = serial.Serial('COM8', 9600)

        def brallie(ch):
            letter = {
                'a': "100000",
                'A': "100000",
                'b': "101000",
                'B': "101000",
                'c': "110000",
                'C': "110000",
                'd': "110100",
                'D': "110100",
                'e': "100100",
                'E': "100100",
                'f': "111000",
                'F': "111000",
                'g': "111100",
                'G': "111100",
                'h': "101100",
                'H': "101100",
                'i': "011000",
                'I': "011000",
                'j': "011100",
                'J': "011100",
                'k': "100010",
                'K': "100010",
                'l': "101010",
                'L': "101010",
                'm': "110010",
                'M': "110010",
                'n': "110110",
                'N': "110110",
                'o': "100110",
                'O': "100110",
                'p': "111010",
                'P': "111010",
                'q': "111110",
                'Q': "111110",
                'r': "101110",
                'R': "101110",
                's': "011010",
                'S': "011010",
                't': "011110",
                'T': "011110",
                'u': "100011",
                'U': "100011",
                'v': "101011",
                'V': "101011",
                'w': "011101",
                'W': "011101",
                'x': "110011",
                'X': "110011",
                'y': "110111",
                'Y': "110111",
                'z': "100111",
                'Z': "100111",
                '0': "010110",
                '1': "100000",
                '2': "110000",
                '3': "100100",
                '4': "100110",
                '5': "100010",
                '6': "110100",
                '7': "110110",
                '8': "110010",
                '9': "010100",
                '*': "001010",
                ',': "010000",
                ';': "011000",
                ':': "010010",
                '.': "010011",
                '!': "011010",
                '(': "011011",
                ')': "011011",
                '?': "011001",
                '"': "001011",
                '#': "001111",
                '-': "001001",
                ' ': "000000"
            }
            return letter.get(ch, "")

        f = open(target2, 'r')
        num = ""
        mystr = f.read()
        i = 0
        while i < len(mystr):
            j = 0
            ch = mystr[i]
            num = num + brallie(ch) + ','
            print(num)
            while j < len(num):
                c = num[j]
                bluetooth.write(str.encode(c))
                print(c)
                time.sleep(0.1)
                j = j + 1
            num = ""
            i = i + 1
            time.sleep(0.1)

        f.close()
        return (str(selected_pdf))       #to check whether it prints on website or not, delete after checking

@app.route('/upload/<filename>')
def send_pdf(filename):
    return send_from_directory(current_user.username, filename)

@app.route('/allfiles')
@login_required
def allfiles():
    target = os.path.join(APP_ROOT, current_user.username)
    pdf_names = os.listdir(target)
    pdf_final = []
    for pdf in pdf_names:
        if pdf.endswith('txt'):
            pdf_final.append(pdf)
    return render_template("allfiles.html", pdf_names=pdf_final)








