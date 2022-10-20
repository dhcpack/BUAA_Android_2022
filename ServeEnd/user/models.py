from django.db import models


# Create your models here.
class User(models.Model):
    nickname = models.CharField(max_length=64, primary_key=True)
    password = models.CharField(max_length=64)
    pic = models.CharField(max_length=64, default="userPic.png")
    days = models.IntegerField(default=0)
    target = models.CharField(max_length=64)
    level = models.IntegerField(default=0)
    stuId = models.CharField(max_length=16)
    institute = models.CharField(max_length=64)
    major = models.CharField(max_length=64)
    grade = models.CharField(max_length=16)
    sex = models.BooleanField(default=True)


class Check(models.Model):
    nickname = models.ForeignKey('User', on_delete=models.CASCADE)
    time = models.DateTimeField(auto_now=True)


class Book(models.Model):
    bookname = models.CharField(max_length=128, default="未命名记忆本")
    nickname = models.ForeignKey('User', on_delete=models.CASCADE)
    pic = models.CharField(max_length=64, default="bookPic.png")
    tag = models.CharField(max_length=64, default="未分类")
    public = models.BooleanField(default=False)


class Ques(models.Model):
    nickname = models.ForeignKey('User', on_delete=models.CASCADE)
    book = models.ForeignKey('Book', on_delete=models.CASCADE)
    type = models.IntegerField()
    ques = models.CharField(max_length=1024)
    ans1 = models.CharField(max_length=1024, null=True)
    ans2 = models.CharField(max_length=1024, null=True)
    ans3 = models.CharField(max_length=1024, null=True)
    ans4 = models.CharField(max_length=1024, null=True)


class LearnRecord(models.Model):
    nickname = models.ForeignKey('User', on_delete=models.CASCADE)
    time = models.DateTimeField(auto_now=True)
    book = models.ForeignKey('Book', on_delete=models.CASCADE)


class Friends(models.Model):
    nickname1 = models.ForeignKey('User', on_delete=models.CASCADE, related_name='nickname1')
    nickname2 = models.ForeignKey('User', on_delete=models.CASCADE, related_name='nickname2')
    time = models.DateTimeField(auto_now=True)


class Request(models.Model):
    sender = models.ForeignKey('User', on_delete=models.CASCADE, related_name='request_sender')
    receiver = models.ForeignKey('User', on_delete=models.CASCADE, related_name='request_receiver')
    time = models.DateTimeField(auto_now=True)
    msg = models.CharField(max_length=1024, default="")


class ChatRecord(models.Model):
    sender = models.ForeignKey('User', on_delete=models.CASCADE, related_name='chat_sender')
    receiver = models.ForeignKey('User', on_delete=models.CASCADE, related_name='chat_receiver')
    time = models.DateTimeField(auto_now=True)
    msg = models.CharField(max_length=1024, default="")


class Post(models.Model):
    nickname = models.ForeignKey('User', on_delete=models.CASCADE)
    time = models.DateTimeField(auto_now=True)
    favor = models.IntegerField(default=0)
    title = models.CharField(max_length=1024, default="Title")
    content = models.CharField(max_length=4096, default="content")
    tag = models.CharField(max_length=32, default="未分类")


class Comment(models.Model):
    postId = models.ForeignKey('Post', on_delete=models.CASCADE)
    nickname = models.ForeignKey('User', on_delete=models.CASCADE)
    time = models.DateTimeField(auto_now=True)
    content = models.CharField(max_length=1024, default="content")
