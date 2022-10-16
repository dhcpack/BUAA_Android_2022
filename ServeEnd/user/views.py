import json

from django.http import JsonResponse
from django.views import View
from rest_framework.status import *

from .serializers import *


# Create your views here.
class UserView(View):
    def get(self, request, nickname, password):
        try:
            user = User.objects.get(nickname=nickname)
        except User.DoesNotExist:
            return JsonResponse({'error': '用户不存在'}, status=HTTP_404_NOT_FOUND)
        if user.password != password:
            return JsonResponse({'error': '密码错误'}, status=HTTP_401_UNAUTHORIZED)
        return JsonResponse(data=UserModelSerializer(instance=user, many=False).data, status=HTTP_200_OK)

    def post(self, request):
        data = json.loads(request.body)
        serializer = UserModelSerializer(data=data)
        if not serializer.is_valid():
            return JsonResponse(dict(serializer.errors))
        serializer.save()  # 传入data调用create，传入instance调用update
        return JsonResponse(serializer.data, status=HTTP_201_CREATED)
