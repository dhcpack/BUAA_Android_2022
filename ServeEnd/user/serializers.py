from rest_framework import serializers

from .models import *


# 模型序列化器
class UserModelSerializer(serializers.ModelSerializer):
    def validate(self, attrs):
        if len(attrs['nickname']) == 0:
            raise serializers.ValidationError(detail="用户名不能为空")
        if len(attrs['nickname']) > 64:
            raise serializers.ValidationError(detail="用户名不能超过64个字符")
        if User.objects.filter(nickname=attrs['nickname']).exists():
            raise serializers.ValidationError(detail="用户名已存在")
        if len(attrs['password']) == 0:
            raise serializers.ValidationError(detail="密码不能为空")
        if len(attrs['password']) > 64:
            raise serializers.ValidationError(detail="密码不能超过64个字符")
        return attrs

    class Meta:
        model = User
        fields = '__all__'
