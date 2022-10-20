from django.urls import re_path
from rest_framework.routers import DefaultRouter

from user.views import *

router = DefaultRouter()
router.register("post", PostModelViewSet)
router.register("comment", CommentModelViewSet)

urlpatterns = [
                  re_path("^signin/(?P<nickname>[_a-zA-Z0-9]+)/(?P<password>[_a-zA-Z0-9]+)/$", UserView.as_view()),
                  re_path("^signup/$", UserView.as_view()),
                  re_path("^modifydetail/$", UserView.as_view()),

                  re_path("^checkdetail/(?P<nickname>[_a-zA-Z0-9]+)/$", CheckView.as_view()),
                  re_path("^check/$", CheckView.as_view()),

                  re_path("^getbooks/(?P<nickname>[_a-zA-Z0-9]+)/$", BookView.as_view()),
                  re_path("^addbook/$", BookView.as_view()),
                  re_path("^modifybook/$", BookView.as_view()),
                  re_path("^deletebook/(?P<nickname>[_a-zA-Z0-9]+)/(?P<bookname>[\u4e00-\u9fa5_a-zA-Z0-9]+)/$",
                          BookView.as_view()),

                  re_path("^getques/(?P<book_id>[\u4e00-\u9fa5_a-zA-Z0-9]+)/$",
                          QuesView.as_view()),
                  re_path("^addques/$", QuesView.as_view()),
                  re_path("^modifyques/$", QuesView.as_view()),
                  re_path(
                      "^deleteques/(?P<nickname>[_a-zA-Z0-9]+)/(?P<bookid>[\u4e00-\u9fa5_a-zA-Z0-9]+)/(?P<quesid>[0-9]+)/$",
                      QuesView.as_view()),

                  re_path("^getrequests/(?P<nickname>[_a-zA-Z0-9]+)/send/$", RequestView.as_view()),
                  re_path("^getrequests/(?P<nickname>[_a-zA-Z0-9]+)/receive/$", RequestView.as_view()),
                  re_path("^postrequest/$", RequestView.as_view()),

                  re_path("^getfriends/(?P<nickname>[_a-zA-Z0-9]+)/$", FriendsView.as_view()),
                  re_path("^acceptrequest/$", FriendsView.as_view()),
                  re_path("^deletefriend/(?P<nickname1>[_a-zA-Z0-9]+)/(?P<nickname2>[_a-zA-Z0-9]+)/$",
                          FriendsView.as_view()),

                  re_path("^getchatrecord/(?P<sender>[_a-zA-Z0-9]+)/(?P<receiver>[_a-zA-Z0-9]+)/$", ChatView.as_view()),
                  re_path("^chat/$", ChatView.as_view()),

                  re_path("^a/(?P<postid>[0-9]+)/oppose/$", FavourView.as_view()),
                  re_path("^a/(?P<postid>[0-9]+)/favour/$", FavourView.as_view()),

                  re_path("^searchuser/(?P<column>[_a-zA-Z0-9]+)/(?P<cond>[\u4e00-\u9fa5_a-zA-Z0-9]+)/$",
                          SearchUserView.as_view()),
                  re_path("^searchbook/(?P<column>[_a-zA-Z0-9]+)/(?P<cond>[\u4e00-\u9fa5_a-zA-Z0-9]+)/$",
                          SearchBookView.as_view()),
                  re_path("^searchpost/(?P<column>[_a-zA-Z0-9]+)/(?P<cond>[\u4e00-\u9fa5_a-zA-Z0-9]+)/$",
                          SearchPostView.as_view()),
                  re_path("^searchcomment/(?P<postid>[0-9]+)/$", SearchCommentView.as_view()),

                  re_path("^importbook/(?P<nickname>[_a-zA-Z0-9]+)/(?P<bookid>[0-9]+)/$", ImportBookView.as_view()),

                  # re_path("^getimage/(?P<path>[_a-zA-Z0-9.]+)/$", GetImageView.as_view()),

              ] + router.urls
