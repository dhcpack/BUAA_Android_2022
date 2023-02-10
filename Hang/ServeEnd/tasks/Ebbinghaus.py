import datetime

reviewTime = [1, 2, 4, 7, 15]


def ebbinghausReviewTime(stage):
    if stage >= len(reviewTime):
        stage = len(reviewTime) - 1
    return datetime.datetime.now() + datetime.timedelta(reviewTime[stage]) + datetime.timedelta(hours=8)
