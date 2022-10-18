import json

from django.http import JsonResponse
from django.views import View
from rest_framework.status import *

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
