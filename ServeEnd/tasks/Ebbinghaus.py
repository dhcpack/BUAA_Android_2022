import datetime

reviewTime = [1, 2, 4, 7, 15]


def ebbinghausReviewTime(stage):
    return datetime.datetime.now() + datetime.timedelta(reviewTime[stage]) + datetime.timedelta(hours=8)
