From openjdk:8
copy ./target/codingexercises-1.0.0.jar codingexercises-1.0.0.jar
CMD ["java","-jar","codingexercises-1.0.0.jar"]