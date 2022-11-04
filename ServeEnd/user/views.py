import datetime
import json

from django.http import JsonResponse, HttpResponse
from django.views import View
from rest_framework.status import *
from rest_framework.viewsets import ModelViewSet

from .serializers import *


# Create your views here.
class UserView(View):
    def get(self, request, nickname, password):  # 登录
        try:
            user = User.objects.get(nickname=nickname)
        except User.DoesNotExist:
            return JsonResponse({'error': '用户不存在'}, status=HTTP_404_NOT_FOUND)
        if user.password != password:
            return JsonResponse({'error': '密码错误'}, status=HTTP_401_UNAUTHORIZED)
        return JsonResponse(data=UserModelSerializer(instance=user, many=False).data, status=HTTP_200_OK)

    def post(self, request):  # 注册
        data = json.loads(request.body)
        serializer = UserModelSerializer(data=data)
        if User.objects.filter(nickname=data["nickname"]).exists():
            return JsonResponse({'error': '用户名已存在'}, status=HTTP_401_UNAUTHORIZED)
        if not serializer.is_valid():
            return JsonResponse(dict(serializer.errors))
        serializer.save()  # 传入data调用create，传入instance调用update
        return JsonResponse(serializer.data, status=HTTP_201_CREATED)

    def put(self, request):  # 修改
        data = json.loads(request.body)
        try:
            user = User.objects.get(nickname=data["nickname"])
        except User.DoesNotExist:
            return JsonResponse({'error': '用户不存在'}, status=HTTP_404_NOT_FOUND)
        serializer = UserModelSerializer(data=data, instance=user)
        if not serializer.is_valid():
            return JsonResponse(dict(serializer.errors))
        serializer.save()  # 传入data调用create，传入instance调用update
        return JsonResponse(serializer.data, status=HTTP_201_CREATED)


class CheckView(View):
    def get(self, request, nickname):
        try:
            user = User.objects.get(nickname=nickname)
        except User.DoesNotExist:
            return JsonResponse({'error': '用户不存在'}, status=HTTP_404_NOT_FOUND)
        days = user.days
        if days != 0:
            lastRecord = Check.objects.filter(nickname=nickname).last().time.astimezone()
        else:
            lastRecord = "None"

        return JsonResponse(data={"lastCheckRecord": lastRecord, "totalDays": days}, status=HTTP_200_OK)

    def post(self, request):
        data = json.loads(request.body)
        try:
            user = User.objects.get(nickname=data["nickname"])
        except User.DoesNotExist:
            return JsonResponse({'error': '用户不存在'}, status=HTTP_404_NOT_FOUND)
        serializer = CheckModelSerializer(data=data)
        if not serializer.is_valid():
            return JsonResponse(dict(serializer.errors))
        user.days += 1
        user.save()
        serializer.save()
        return JsonResponse(serializer.data, status=HTTP_201_CREATED)


class BookView(View):
    def get(self, request, nickname):
        try:
            User.objects.get(nickname=nickname)
        except User.DoesNotExist:
            return JsonResponse([{'error': '用户不存在'}], status=HTTP_404_NOT_FOUND, safe=False)
        books = Book.objects.filter(nickname=nickname)
        return JsonResponse(data=BookModelSerializer(instance=books, many=True).data, status=HTTP_200_OK, safe=False)

    def post(self, request):
        data = json.loads(request.body)
        try:
            User.objects.get(nickname=data["nickname"])
        except User.DoesNotExist:
            return JsonResponse({'error': '用户不存在'}, status=HTTP_404_NOT_FOUND)
        if Book.objects.filter(nickname=data["nickname"]).filter(bookname=data["bookname"]).exists():
            return JsonResponse({'error': '同名记忆本'}, status=HTTP_401_UNAUTHORIZED)
        serializer = BookModelSerializer(data=data)
        if not serializer.is_valid():
            return JsonResponse(dict(serializer.errors))
        serializer.save()
        return JsonResponse(serializer.data, status=HTTP_201_CREATED)

    def put(self, request):
        data = json.loads(request.body)
        try:
            User.objects.get(nickname=data["nickname"])
        except User.DoesNotExist:
            return JsonResponse({'error': '用户不存在'}, status=HTTP_404_NOT_FOUND)
        try:
            book = Book.objects.filter(nickname=data["nickname"]).get(bookname=data["bookname"])
        except Book.DoesNotExist:
            return JsonResponse({'error': '记忆本不存在'}, status=HTTP_404_NOT_FOUND)
        serializer = BookModelSerializer(data=data, instance=book)
        if not serializer.is_valid():
            return JsonResponse(dict(serializer.errors))
        serializer.save()
        return JsonResponse(serializer.data, status=HTTP_201_CREATED)

    def delete(self, request, nickname, bookname):
        try:
            book = Book.objects.filter(nickname=nickname).get(bookname=bookname)
        except Book.DoesNotExist:
            return JsonResponse({'msg': '成功删除'}, status=HTTP_201_CREATED)
        book.delete()
        return JsonResponse({'msg': '成功删除'}, status=HTTP_201_CREATED)


class QuesView(View):
    def get(self, request, book_id):
        try:
            book = Book.objects.get(id=book_id)
        except Book.DoesNotExist:
            return JsonResponse([{'error': '记忆本不存在'}], status=HTTP_404_NOT_FOUND, safe=False)
        questions = Ques.objects.filter(book=book_id)
        # add to learn record table
        LearnRecord.objects.create(nickname=book.nickname,
                                   book=Book.objects.get(id=book_id))
        return JsonResponse(data=QuesModelSerializer(instance=questions, many=True).data, status=HTTP_200_OK,
                            safe=False)

    def post(self, request):
        data = json.loads(request.body)
        serializer = QuesModelSerializer(data=data)
        if not serializer.is_valid():
            return JsonResponse(dict(serializer.errors))
        serializer.save()  # 传入data调用create，传入instance调用update
        # add to learn record table
        LearnRecord.objects.create(nickname=User.objects.get(nickname=data['nickname']),
                                   book=Book.objects.get(id=data['book']))
        return JsonResponse(serializer.data, status=HTTP_201_CREATED)

    def put(self, request):
        data = json.loads(request.body)
        try:
            ques = Ques.objects.get(id=data['id'])
        except Ques.DoesNotExist:
            return JsonResponse({'error': '题目不存在'}, status=HTTP_404_NOT_FOUND)
        serializer = QuesModelSerializer(data=data, instance=ques)
        if not serializer.is_valid():
            return JsonResponse(dict(serializer.errors))
        serializer.save()  # 传入data调用create，传入instance调用update
        # add to learn record table
        LearnRecord.objects.create(nickname=User.objects.get(nickname=data['nickname']),
                                   book=Book.objects.get(id=data['book']))
        return JsonResponse(serializer.data, status=HTTP_201_CREATED)

    def delete(self, request, nickname, bookid, quesid):
        try:
            ques = Ques.objects.filter(nickname=nickname, book=bookid).get(id=quesid)
        except Ques.DoesNotExist:
            return JsonResponse({'msg': '成功删除'}, status=HTTP_201_CREATED)
        ques.delete()
        return JsonResponse({'msg': '成功删除'}, status=HTTP_201_CREATED)


class RequestView(View):
    def get(self, request, nickname):
        try:
            user = User.objects.get(nickname=nickname)
        except User.DoesNotExist:
            return JsonResponse({'error': '用户不存在'}, status=HTTP_404_NOT_FOUND)
        if request.path[-8:] == "receive/":
            requests = Request.objects.filter(receiver=user)
        else:
            requests = Request.objects.filter(sender=user)
        return JsonResponse(data=RequestModelSerializer(instance=requests, many=True).data, status=HTTP_200_OK,
                            safe=False)

    def post(self, request):
        data = json.loads(request.body)
        if Request.objects.filter(sender=data['sender'], receiver=data['receiver']).exists():
            return JsonResponse(data={"error": "您已发送过好友申请"})
        serializer = RequestModelSerializer(data=data)
        if not serializer.is_valid():
            return JsonResponse(serializer.errors, status=HTTP_404_NOT_FOUND)
        serializer.save()
        return JsonResponse(data=serializer.data, status=HTTP_200_OK)


class FriendsView(View):
    def get(self, request, nickname):
        try:
            user = User.objects.get(nickname=nickname)
        except User.DoesNotExist:
            return JsonResponse({'error': '用户不存在'}, status=HTTP_404_NOT_FOUND)
        friends = Friends.objects.filter(nickname1=user)
        return JsonResponse(data=FriendModelSerializer(instance=friends, many=True).data, status=HTTP_200_OK,
                            safe=False)

    def post(self, request):
        data = json.loads(request.body)
        try:  # nickname1 is receiver
            receiver = User.objects.get(nickname=data['receiver'])
            sender = User.objects.get(nickname=data['sender'])
        except User.DoesNotExist:
            return JsonResponse({'error': '用户不存在'}, status=HTTP_404_NOT_FOUND)
        Friends.objects.create(nickname1=receiver, nickname2=sender)
        Friends.objects.create(nickname1=sender, nickname2=receiver)
        request = Request.objects.get(receiver=receiver, sender=sender)
        ChatRecord.objects.create(sender=sender, receiver=receiver, msg=request.msg)
        ChatRecord.objects.create(sender=receiver, receiver=sender, msg="我通过了你的朋友验证请求，现在我们可以开始聊天了")
        request.delete()
        return JsonResponse(data={'msg': '成功添加好友'}, status=HTTP_201_CREATED)

    def delete(self, request, nickname1, nickname2):
        try:  # 1 删除 2
            user1 = User.objects.get(nickname=nickname1)
            user2 = User.objects.get(nickname=nickname2)
        except User.DoesNotExist:
            return JsonResponse({'error': '用户不存在'}, status=HTTP_404_NOT_FOUND)
        Friends.objects.get(nickname1=user1, nickname2=user2).delete()
        Friends.objects.get(nickname1=user2, nickname2=user1).delete()
        ChatRecord.objects.create(sender=user1, receiver=user2, msg="已将你移除好友列表")
        return JsonResponse(data={'msg': '成功删除好友'}, status=HTTP_201_CREATED)


class ChatView(View):
    def get(self, request, sender, receiver):
        try:
            sender = User.objects.get(nickname=sender)
            receiver = User.objects.get(nickname=receiver)
        except User.DoesNotExist:
            return JsonResponse({'error': '用户不存在'}, status=HTTP_404_NOT_FOUND)
        record = ChatRecord.objects.filter(sender=sender, receiver=receiver)
        return JsonResponse(data=ChatRecordModelSerializer(instance=record, many=True).data, status=HTTP_200_OK,
                            safe=False)

    def post(self, request):
        data = json.loads(request.body)
        serializer = ChatRecordModelSerializer(data=data)
        if not serializer.is_valid():
            return JsonResponse(serializer.errors, status=HTTP_404_NOT_FOUND)
        serializer.save()
        return JsonResponse(data=serializer.data, status=HTTP_201_CREATED)


class PostModelViewSet(ModelViewSet):
    queryset = Post.objects.all().order_by("-time")
    serializer_class = PostModelSerializer


class CommentModelViewSet(ModelViewSet):
    queryset = Comment.objects.all().order_by("-time")
    serializer_class = CommentModelSerializer


class FavourView(View):
    def get(self, request, postid):
        try:
            post = Post.objects.get(id=postid)
        except User.DoesNotExist:
            return JsonResponse({'error': '帖子'}, status=HTTP_404_NOT_FOUND)
        if request.path[:-7] == "oppose/":
            post.favor -= 1
        else:
            post.favor += 1
        post.save()
        return JsonResponse(PostModelSerializer(instance=post).data, status=HTTP_201_CREATED)


class SearchUserView(View):
    def get(self, request, column, cond):
        if column == "nickname":
            try:
                user = User.objects.get(nickname=cond)
            except User.DoesNotExist:
                return JsonResponse({"error": "用户不存在"}, status=HTTP_404_NOT_FOUND)
            return JsonResponse(UserModelSerializer(instance=user).data, status=HTTP_200_OK)
        elif column == "institute":
            users = User.objects.filter(institute=cond)
            return JsonResponse(UserModelSerializer(instance=users, many=True).data, status=HTTP_200_OK, safe=False)
        elif column == "major":
            users = User.objects.filter(major=cond)
            return JsonResponse(UserModelSerializer(instance=users, many=True).data, status=HTTP_200_OK, safe=False)
        elif column == "grade":
            users = User.objects.filter(grade=cond)
            return JsonResponse(UserModelSerializer(instance=users, many=True).data, status=HTTP_200_OK, safe=False)
        elif column == "sex":
            users = User.objects.filter(sex=cond)
            return JsonResponse(UserModelSerializer(instance=users, many=True).data, status=HTTP_200_OK, safe=False)
        elif column == "all":
            users = User.objects.all()
            return JsonResponse(UserModelSerializer(instance=users, many=True).data, status=HTTP_200_OK, safe=False)


class SearchBookView(View):
    def get(self, request, column, cond):
        if column == "tag":
            books = Book.objects.filter(public=True).filter(tag=cond)
            return JsonResponse(BookModelSerializer(instance=books, many=True).data, status=HTTP_200_OK, safe=False)
        else:
            books = Book.objects.filter(public=True)
            return JsonResponse(BookModelSerializer(instance=books, many=True).data, status=HTTP_200_OK, safe=False)


class SearchPostView(View):
    def get(self, request, column, cond):
        if column == "tag":
            posts = Post.objects.filter(tag=cond)
            return JsonResponse(PostModelSerializer(instance=posts, many=True).data, status=HTTP_200_OK, safe=False)
        else:
            posts = Post.objects.all()
            return JsonResponse(PostModelSerializer(instance=posts, many=True).data, status=HTTP_200_OK, safe=False)


class SearchCommentView(View):
    def get(self, request, postid):
        try:
            Post.objects.get(id=postid)
        except Post.DoesNotExist:
            return JsonResponse({"error": "帖子不存在"}, status=HTTP_404_NOT_FOUND)
        comments = Comment.objects.filter(postId=postid)
        return JsonResponse(CommentModelSerializer(instance=comments, many=True).data, status=HTTP_200_OK, safe=False)


class ImportBookView(View):
    def get(self, request, nickname, bookid):
        try:
            user = User.objects.get(nickname=nickname)
            book = Book.objects.get(id=bookid)
        except User.DoesNotExist or Book.DoesNotExist:
            return JsonResponse({"error": "用户或记忆本不存在"}, status=HTTP_404_NOT_FOUND)
        if book.nickname == user:
            return JsonResponse({"msg": "已导入"}, status=HTTP_200_OK)
        if Book.objects.filter(nickname=user.nickname, bookname=book.bookname).exists():
            return JsonResponse({"error": "导入失败，已存在同名记忆本"}, status=HTTP_401_UNAUTHORIZED)
        nbook = Book.objects.create(bookname=book.bookname, pic=book.pic, tag=book.tag, nickname=user)
        ques = Ques.objects.filter(book=book)
        for q in ques:
            Ques.objects.create(type=q.type, ans1=q.ans1, ans2=q.ans2, ans3=q.ans3, ans4=q.ans4, book=nbook,
                                nickname=user)
        return JsonResponse({"msg": "已导入"}, status=HTTP_200_OK)


class GetImageView(View):
    def get(self, request, path):
        with open("./static/" + path, 'rb') as f:
            image = f.read()
        return HttpResponse(image, content_type="image/png")

    def post(self, request):
        picture = request.FILES['pic']
        backEndName = datetime.datetime.now().strftime('%Y%m%d%H%I%S') + ".png"
        with open("./static/" + backEndName, 'wb') as f:
            for content in picture.chunks():
                f.write(content)
        return JsonResponse({"path": backEndName}, status=HTTP_201_CREATED)
