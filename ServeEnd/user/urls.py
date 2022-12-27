from django.urls import re_path, path
from rest_framework.routers import DefaultRouter

from user.views import *

router = DefaultRouter()
router.register("post", PostModelViewSet)
router.register("comment", CommentModelViewSet)

urlpatterns = [
                  re_path("^signin/(?P<nickname>[_a-zA-Z0-9]+)/(?P<password>[_a-zA-Z0-9]+)/$", UserView.as_view()),
                  re_path("^signup/$", UserView.as_view()),
                  re_path("^modifydetail/$", UserView.as_view()),
                  re_path("^userdetail/(?P<nickname>[_a-zA-Z0-9]+)/$", UserDetailView.as_view()),

                  re_path("^checkdetail/(?P<nickname>[_a-zA-Z0-9]+)/$", CheckView.as_view()),
                  re_path("^check/$", CheckView.as_view()),

                  re_path("^getbooks/(?P<nickname>[_a-zA-Z0-9]+)/$", BookView.as_view()),
                  re_path("^addbook/$", BookView.as_view()),
                  re_path("^modifybook/$", BookView.as_view()),
                  re_path("^deletebook/(?P<nickname>[_a-zA-Z0-9]+)/(?P<bookId>[\u4e00-\u9fa5_a-zA-Z0-9]+)/$",
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

                  re_path("^getfile/(?P<path>[_a-zA-Z0-9]+)/(?P<type>[_a-zA-Z0-9]+)/$", FileView.as_view()),
                  re_path("^postfile/(?P<type>[_a-zA-Z0-9]+)/$", FileView.as_view()),

                  re_path("^learningbook/(?P<nickname>[_a-zA-Z0-9]+)/$", LearningBookView.as_view()),
                  re_path("^setbook/(?P<nickname>[_a-zA-Z0-9]+)/(?P<bookId>[0-9]+)/$", LearningBookView.as_view()),
                  re_path("^setprocess/(?P<nickname>[_a-zA-Z0-9]+)/(?P<bookId>[0-9]+)/(?P<process>[0-9]+)/$", ProcessView.as_view()),
                  path("getpublicbook/", PublicBookView.as_view()),

                  re_path("^getreviewcount/(?P<nickname>[_a-zA-Z0-9]+)/$", ReviewCountView.as_view()),
                  re_path("^getreviewques/(?P<nickname>[_a-zA-Z0-9]+)/(?P<type>[_a-zA-Z0-9]+)/$", ReviewView.as_view()),
                  re_path("^review/(?P<quesId>[0-9]+)/$", ReviewView.as_view()),

                  path("ocr/", OCRView.as_view()),

                  path("recom/", RecommendView.as_view()),
re_path("^search/(?P<quesId>[0-9]+)/$", ReviewView.as_view()),

              ] + router.urls
