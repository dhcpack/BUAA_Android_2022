from django.db import models


# Create your models here.
class User(models.Model):
    nickname = models.CharField(max_length=64)
    password = models.CharField(max_length=64)
    pic = models.CharField(max_length=64, default="/static/default/userPic.png")
    days = models.IntegerField(default=0)
    target = models.CharField(max_length=64)
    level = models.IntegerField(default=0)
    stuId = models.CharField(max_length=16)
    institute = models.CharField(max_length=64)
    major = models.CharField(max_length=64)
    grade = models.CharField(max_length=16)


class Check(models.Model):
    userId = models.ForeignKey('User', on_delete=models.CASCADE)
    time = models.DateTimeField(auto_now=True)


class Book(models.Model):
    userId = models.ForeignKey('User', on_delete=models.CASCADE)
    pic = models.CharField(max_length=64, default="/static/default/bookPic.png")
    tag = models.CharField(max_length=64, default="未分类")
    public = models.BooleanField(default=False)


class Ques(models.Model):
    bookId = models.ForeignKey('Book', on_delete=models.CASCADE)
    type = models.IntegerField()
    ques = models.CharField(max_length=1024)
    ans1 = models.CharField(max_length=1024, null=True)
    ans2 = models.CharField(max_length=1024, null=True)
    ans3 = models.CharField(max_length=1024, null=True)
    ans4 = models.CharField(max_length=1024, null=True)


class LearnRecord(models.Model):
    userId = models.ForeignKey('User', on_delete=models.CASCADE)
    time = models.DateTimeField(auto_now=True)
    bookId = models.ForeignKey('Book', on_delete=models.CASCADE)


class Friends(models.Model):
    userId1 = models.ForeignKey('User', on_delete=models.CASCADE, related_name='userId1')
    userId2 = models.ForeignKey('User', on_delete=models.CASCADE, related_name='userId2')
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
    userId = models.ForeignKey('User', on_delete=models.CASCADE)
    time = models.DateTimeField(auto_now=True)
    favor = models.IntegerField(default=0)
    title = models.CharField(max_length=1024, default="Title")
    content = models.CharField(max_length=4096, default="content")


class Comment(models.Model):
    postId = models.ForeignKey('Post', on_delete=models.CASCADE)
    userId = models.ForeignKey('User', on_delete=models.CASCADE)
    time = models.DateTimeField(auto_now=True)
    content = models.CharField(max_length=1024, default="content")
