# GitView 📱

GitView is a modern Android app that allows users to explore GitHub repositories by categories, perform keyword-based searches, view repository details, and get AI-generated summaries using OpenAI.  
It is built with **Jetpack Compose**, follows **MVVM** and **Clean Architecture**, and integrates **GitHub API** & **OpenAI API**.

---

## ✨ Features

- 🔍 Search and explore GitHub repositories
- 🗂️ Categorized browsing by programming languages
- 📁 Repo detail screen with stars, forks, and language data
- 🤖 AI-generated summaries of repository README content (via OpenAI)
- ⭐ Favorite repositories and access them easily
- ✅ Fully built using Clean Architecture (data / domain / presentation layers)

---

## 📸 Screenshots

| Home Screen | Repo List Screen |
|-------------|------------------|
| ![Home](screenshots/gitview-main-screen.jpg) | ![RepoList](screenshots/gitview-list-screen.jpg) |

| Repo Detail Screen | AI Summary Screen |
|--------------------|-------------------|
| ![Detail](screenshots/gitview-detail-screen.jpg) | ![AI](screenshots/gitview-aisummary-screen.jpg) |

---

## 🧱 Tech Stack

- **Language**: Kotlin
- **UI**: Jetpack Compose
- **Architecture**: MVVM + Clean Architecture
- **Dependency Injection**: Hilt
- **Networking**: Retrofit
- **Local Storage**: Room
- **Pagination**: Paging 3
- **Markdown Rendering**: [MarkdownText](https://github.com/jeziellago/compose-markdown)
- **AI Integration**: OpenAI API

---

## 🔐 Setup Instructions

Before running the project, you need to configure your `local.properties` file to include your API keys.

```properties
# local.properties

OPENAI_API_KEY="sk-xxxxxx..."         # Your OpenAI API key
GITHUB_TOKEN="ghp_xxxxxxxxxxxxxx..."  # Your GitHub Personal Access Token
```

> ℹ️ Your GitHub token must have access to the `public_repo` scope.

---

## 📁 Project Structure

```
GitView/
│
├── data/
│   ├── remote/       # Retrofit APIs, DTOs
│   ├── local/        # Room DB, DAO, Entity
│   └── repository/   # Implementation of repositories
│
├── domain/
│   ├── model/        # Clean domain models
│   ├── use_case/     # Use cases per business logic
│   └── repository/   # Abstract repository interfaces
│
├── presentation/
│   ├── ui/
│   │   ├── home/         # Home screen (favorites + categories)
│   │   ├── repolist/     # Repo listing with paging
│   │   └── detail/       # Detail + AI summary tabs
│   └── components/       # Reusable UI elements
│
├── di/               # Hilt module providers
└── utils/            # Constants, Mappers, etc.
```

---

## 📦 API References

- **GitHub API**: Used for fetching repositories, user info, and README files
- **OpenAI API**: Used to summarize README content using GPT models

---

## 📄 License

This project is open-source and available under the [MIT License](LICENSE).

---

## 🙌 Acknowledgements

Special thanks to the following tools and services:

- [OpenAI](https://openai.com/)
- [GitHub REST API](https://docs.github.com/en/rest)
- [Jetpack Compose](https://developer.android.com/jetpack/compose)

---

> Developed with ❤️ by [@tyfnsk](https://github.com/tyfnsk)
