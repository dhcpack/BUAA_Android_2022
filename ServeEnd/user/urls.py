from django.urls import re_path
from rest_framework.routers import DefaultRouter

from user.views import *

router = DefaultRouter()

urlpatterns = [
                  re_path("^signin/(?P<nickname>[_a-zA-Z0-9]+)/(?P<password>[_a-zA-Z0-9]+)/$", UserView.as_view()),
                  re_path("^signup/", UserView.as_view()),
              ] + router.urls
