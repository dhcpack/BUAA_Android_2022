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


class BookModelSerializer(serializers.ModelSerializer):
    class Meta:
        model = Book
        fields = '__all__'


class QuesModelSerializer(serializers.ModelSerializer):
    def validate(self, attrs):
        # print(attrs['nickname'])
        # if attrs['nickname'] is None:
        #     raise serializers.ValidationError(detail="用户不存在")
        # if attrs['bookname'] is None:
        #     raise serializers.ValidationError(detail="记忆本不存在")
        # attrs: dict
        if not ((attrs['type'] == 1 and attrs.get('ans1') is not None) or
                (attrs['type'] == 2 and attrs.get('ans2') is not None) or
                (attrs['type'] == 3 and attrs.get('ans3') is not None) or
                (attrs['type'] == 4 and attrs.get('ans4') is not None)):
            raise serializers.ValidationError(detail="类型和答案位置不匹配")
        return attrs

    class Meta:
        model = Ques
        fields = '__all__'


class FriendModelSerializer(serializers.ModelSerializer):
    class Meta:
        model = Friends
        fields = '__all__'


class RequestModelSerializer(serializers.ModelSerializer):
    class Meta:
        model = Request
        fields = '__all__'


class ChatRecordModelSerializer(serializers.ModelSerializer):
    class Meta:
        model = ChatRecord
        fields = '__all__'


class PostModelSerializer(serializers.ModelSerializer):
    class Meta:
        model = Post
        fields = '__all__'


class CommentModelSerializer(serializers.ModelSerializer):
    class Meta:
        model = Comment
        fields = '__all__'
