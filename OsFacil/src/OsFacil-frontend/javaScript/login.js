const API_URL = "http://localhost:8080";

document.addEventListener("DOMContentLoaded", () => {
    const form = document.getElementById("loginForm");

    if (!form) {
        console.error("LoginForm não encontrado no HTML!");
        return;
    }

    form.addEventListener("submit", login);
});

/**
 * Alterna visibilidade da senha
 */
function togglePassword() {
    const passwordInput = document.getElementById("password");
    const toggleBtn = document.querySelector(".toggle-password");

    if (passwordInput.type === "password") {
        passwordInput.type = "text";
        toggleBtn.textContent = "🙈";
    } else {
        passwordInput.type = "password";
        toggleBtn.textContent = "👁️";
    }
}

/**
 * Exibe mensagem de erro
 */
function showError(message) {
    const errorDiv = document.getElementById("errorMessage");
    errorDiv.textContent = message;
    errorDiv.style.display = "block";

    // Remove erro após 5 segundos
    setTimeout(() => {
        errorDiv.style.display = "none";
    }, 5000);
}

/**
 * Limpa mensagem de erro
 */
function clearError() {
    const errorDiv = document.getElementById("errorMessage");
    errorDiv.style.display = "none";
}

/**
 * Realiza login
 */
async function login(event) {
    event.preventDefault();

    const username = document.getElementById("username").value.trim();
    const password = document.getElementById("password").value;
    const form = document.getElementById("loginForm");
    const btnLogin = form.querySelector(".btn-login");
    const btnText = btnLogin.querySelector(".btn-text");
    const btnLoader = btnLogin.querySelector(".btn-loader");

    // Validação básica
    if (!username || !password) {
        showError("⚠️ Usuário e senha são obrigatórios");
        return;
    }

    if (password.length < 3) {
        showError("⚠️ Senha inválida");
        return;
    }

    try {
        // Ativa estado de loading
        clearError();
        btnLogin.disabled = true;
        btnText.style.display = "none";
        btnLoader.style.display = "block";

        const response = await fetch(`${API_URL}/auth/login`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({ username, password })
        });

        if (!response.ok) {
            if (response.status === 401) {
                showError("❌ Usuário ou senha incorretos");
            } else if (response.status === 400) {
                showError("❌ Dados inválidos");
            } else {
                showError("❌ Erro ao fazer login. Tente novamente.");
            }
            return;
        }

        const dados = await response.json();

        // Valida resposta
        if (!dados.token) {
            showError("❌ Erro ao processar resposta do servidor");
            return;
        }

        // Armazena dados
        localStorage.setItem("token", dados.token);
        localStorage.setItem("perfil", dados.perfil || "");
        localStorage.setItem("empresaId", dados.empresaId || "");

        // Redireciona após sucesso
        setTimeout(() => {
            window.location.href = "index.html";
        }, 300);

    } catch (error) {
        console.error("Erro:", error);
        showError("🔌 Erro ao conectar com o servidor");
    } finally {
        // Desativa estado de loading
        btnLogin.disabled = false;
        btnText.style.display = "inline";
        btnLoader.style.display = "none";
    }
}