FROM frolvlad/alpine-oraclejdk8:slim
MAINTAINER Nish Tahir <nishtahir@outlook.com>

COPY build/install/linkbait-server /home/linkbait-server
# CMD ["sh",""]
RUN apk update && apk add bash

ENTRYPOINT /home/linkbait-server/bin/linkbait-server
