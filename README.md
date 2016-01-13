# Vending-Machine
Android

Introduction

Write a program or library that simulates a vending machine with the following features:

The implementation should track current stock levels of the machine.
The interface should include methods to restock the machine and to purchase an item.
When a purchase is made, the machine should dispense the correct change (including the number and denomination of each coin).
Assume the machine holds enough coins and currency that it won’t run out of change. You only need to support a single currency, but choose denominations such that the machine can dispense at least 6 different coins or notes (e.g. 1p, 2p, 5p, 10p, 20p, 50p, £1 in Sterling, or 1¢, 5¢, 10¢, 25¢ coins and $1 and $5 notes in USD).

The implementation should be capable of dispensing at least five distinct products (that is, the user should be able to choose from at least 5 different options).

Features developed:

- It tracks current stock levels.
- It restocks the machine using Pull-to-Refresh.
- User can use different denominations for paying a product.
- Vending Machine gives money back in different denominations.
- Dynamic data (products information) got from a service.
- User can Cancel/Confirm a product purchase.
- User can visualice the product availability.
- Material Design (CardViews, Color, Shadows, Transitions)
- Fragments.
- Asynctask
- Recycler View
- ViewHolder pattern