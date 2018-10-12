
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

@app.route("/")
@app.route("/home")
def home():
    return render_template('home.html', posts=posts)


@app.route("/contact")
def contact():
    return render_template('contact.html', title='Contact')
