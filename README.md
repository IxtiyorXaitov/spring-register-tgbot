# Spring Boot Telegram Bot with User Registration & Localization
(Example Bot Name: SpringRegisterTgBot)

A sophisticated Spring Boot 3.5.0 / Java 21 project demonstrating webhook integration with the Telegram Bots API. This bot guides users through a multi-language registration process, including phone number sharing, OTP verification, and collecting personal details.

## Features

*   **Webhook Integration:** Receives updates directly from Telegram via HTTP POST requests.
*   **User Registration Flow:**
    *   **Language Selection:** Supports English, Russian, and Uzbek.
    *   **Phone Number Sharing:** Securely requests and receives user phone numbers using Telegram's contact sharing feature.
    *   **OTP Verification:** Simulates sending and verifying a One-Time Password (OTP) for phone number validation.
    *   **Personal Details:** Collects the user's first and last name.
*   **Multi-language Support (i18n):** All user-facing messages are managed via properties files for easy localization.
*   **State Management:** Utilizes a state machine pattern to track the user's progress through the registration flow.
*   **Interactive Keyboards:** Employs inline and reply keyboards for user interaction.
*   **Modular Design:** Follows best practices with a clear separation of concerns (Controllers, Services, Handlers, Repositories).
*   **Programmatic Webhook Setup:** Automatically registers the webhook with Telegram upon application startup.

## Prerequisites

Before you begin, ensure you have the following installed and configured:

1.  **Java Development Kit (JDK) 21:** (Or a compatible version for Spring Boot 3.5.0)
2.  **Maven:** For building and dependency management.
3.  **Telegram Bot Token:** Obtain a token from [BotFather](https://telegram.me/botfather) on Telegram.
4.  **Publicly Accessible HTTPS URL:** For local development, `ngrok` is highly recommended to expose your local server to the internet.

## Project Setup

### 1. Clone the Repository

```bash
git clone git@github.com:IxtiyorXaitov/spring-register-tgbot.git
cd spring-register-tgbot
