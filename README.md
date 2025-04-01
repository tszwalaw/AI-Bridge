# AI-Bridge

## Description
This Spring Boot application template provides a flexible and extensible framework for integrating multiple AI models into your application. It allows you to switch between different AI models seamlessly, making it easy to scale and adapt your system to different needs

## Features
- AI Model Switching: Easily switch between different AI models with minimal code changes.
- Spring Boot Integration: Fully integrated with Spring Boot, ensuring a robust and production-ready setup.
- Extensible Architecture: Add new AI models or switch between existing ones without disrupting the application.
- Configuration Flexibility: Configure AI models and their parameters via application properties.
- RESTful API: Expose endpoints for interacting with the AI models in a RESTful manner.
- Scalable: Easily scalable for different environments (development, production) and cloud platforms.

## Prerequisites
- Java 17+ (JDK)
- Maven
- Spring Boot

## AI Model
### AI model token
To access the AI models, you will need an API token or authentication key for the AI services. Depending on the model provider, you may need to sign up and obtain this token. Some examples of AI model providers include OpenAI, Hugging Face, or custom AI solutions. Make sure you have the token ready and configured in your environment.
### Local AI Model
If you prefer to run AI models locally instead of using cloud-based models, ensure that the required model is installed and configured on your machine.

To install ollama in Mac
```
brew install ollama
ollama pull llama3.2 3b
export OLLAMA_HOST=127.0.0.1:3000
ollama pull nomic-embed-text
ollama run llama3.2
```

