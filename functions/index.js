const firebase = require('firebase-admin');
const functions = require("firebase-functions");
const admin = require("firebase-admin");

const { initializeApp, applicationDefault, cert } = require('firebase-admin/app');
const { getFirestore, Timestamp, FieldValue } = require('firebase-admin/firestore');

admin.initializeApp();

exports.firestoreNotifications = functions.firestore
    .document('chats/{chatId}')
    .onUpdate((change, context) => {
        const newValue = change.after.data();
        const messages = newValue["messages"];
        const length = messages.length;
        const recentMessage = messages[length-1];
        const type = recentMessage["type"];
        const user = recentMessage["user"];
        const name = recentMessage["name"];

        if (type == "message") {
            const body = recentMessage["body"];
            const time = recentMessage["time"];

            admin.messaging().sendToTopic(
                "notification_topic",
                {
                    notification: {
                        title:  name,
                        body: body
                    },
                    data: {
                         "user": user
                    }
                }
            );
        } else if (type == "trade") {
            const direction = recentMessage["direction"];
            const lot = recentMessage["lot"];
            const price = recentMessage["price"];
            const ticker = recentMessage["ticker"];
            const time = recentMessage["time"];
            const lotRounded = Math.round(lot * 10000) / 10000;

            var formatter = new Intl.NumberFormat('en-US', {
              style: 'currency',
              currency: 'USD',
            });

            admin.messaging().sendToTopic(
                "notification_topic",
                {
                    notification: {
                        title:  ticker + " " + direction,
                        body: lotRounded + " " + ticker + " @ " + formatter.format(price)
                    },
                    data: {
                        "user": user
                    }
                }
            );
        } else {
        }
    });