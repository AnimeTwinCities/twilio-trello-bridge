FROM java:8

COPY . /var/twellio
RUN cd /var/twellio && ./gradlew

CMD cd /var/twellio && ./gradlew run

EXPOSE 8080
