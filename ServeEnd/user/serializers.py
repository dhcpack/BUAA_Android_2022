from rest_framework import serializers

from .models import *


# 模型序列化器
class UserModelSerializer(serializers.ModelSerializer):
    def validate(self, attrs):
        if len(attrs['nickname']) == 0:
            raise serializers.ValidationError(detail="用户名不能为空")
        if len(attrs['nickname']) > 64:
            raise serializers.ValidationError(detail="用户名不能超过64个字符")
        if len(attrs['password']) == 0:
            raise serializers.ValidationError(detail="密码不能为空")
        if len(attrs['password']) > 64:
            raise serializers.ValidationError(detail="密码不能超过64个字符")
        return attrs

    class Meta:
        model = User
        fields = '__all__'


class CheckModelSerializer(serializers.ModelSerializer):
    class Meta:
        model = Check
        fields = '__all__'
