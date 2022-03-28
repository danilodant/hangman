# Hangman by Danilo Gorjon

This is a simple hangman game developed as a web-application.

To play just press the button writen with `PRESS TO START NEW GAME` and start typying the letters on your keyboard to guess the hidden word before you get hanged.

When you win the game or die trying, game is reseted to be played again.

You can also do that with the reset button.

# Play now on the web

Just go to [hangman-by-danilodant](https://hangman-by-danilodant.herokuapp.com/)

# How to build on you computer

Make sure you have your environment correctly configured with java 8 and maven (I used 3.8.4). Then follow the next steps below.

Copy the codebase to your machine, you can do that with the following command:

> SSH
```shell
git clone https://github.com/danilodant/hangman.git
```

> or HTTPS
```shell
git clone https://github.com/danilodant/hangman.git
```

Go to the project folder:

```shell
cd hangman
```

Compile your code:

```shell
mvn clean package spring-boot:repackage
```

Run the project:
```shell
java -jar target/hangman-1.0.0.jar
```
