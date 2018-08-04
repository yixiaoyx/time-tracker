# Trackattack

This is a Java desktop app for time management. It allows you to track the accumulated time you spent on individual tasks you do. Created by: Naomi Que, Luke Fitzpatrick, Yi Xiao, Winnie Zheng, Jackie Cai.

## Getting started

- **Prerequisites:** 
    - You must have MySQL installed and running, which can be found [here](https://dev.mysql.com/downloads/). Make sure MySQL service is running before you run the app.
    - You also need Java SE Runtime Environment 8, which can be found [here](http://www.oracle.com/technetwork/java/javase/downloads/jre8-downloads-2133155.html). 
    - Make sure the `mysql` and `java` commands are included in your `PATH`. (IMPORTANT!)

- **Steps to run the app:**
    - Download or clone this repository.
    - Find the correct setup script and launcher for your platform:
        - Linux & Mac OS: The setup script is `setup.sh`, and the launcher is `trackattack.sh`.
        - Windows: The setup script is `setup.bat`, and the launcher is `trackattack.bat`.
    - Run the setup script in the root folder of this repository to setup a local database for storing your time tracking entries. You will be prompted to enter your MySQL root username and password. *DO NOT run setup more than once unless the previous run failed!*
    - Run the launcher, also in the root folder, each time you wish to open the app. 
    - Make sure the setup script and launcher have execution permission before running.
    - On first run, the app will contain some sample categories and tasks to help you get started. It is recommended that you go through the [User Guide](https://github.com/yixiaoyx/time-tracker/blob/master/documentation/Help%20Document%20%E2%80%93%20How%20to%20use%20Trackattack.pdf) with these examples to familiarize yourself with the app. Feel free to remove them when you are confident.

- **Design document:** See [Design Document](https://github.com/yixiaoyx/time-tracker/blob/master/documentation/trackattack%20design%20document.pdf) for details.


[//]: # (Run 'table_changes.sql' for database changes.)
