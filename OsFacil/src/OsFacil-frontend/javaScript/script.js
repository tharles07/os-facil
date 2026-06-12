const API_URL = "http://localhost:8080";

/* ==========================
   INIT
========================== */
document.addEventListener("DOMContentLoaded", () => {

    const token = localStorage.getItem("token");

    if (!token) {
        console.warn("Sem token, redirecionando login");
        window.location.href = "login.html";
        return;
    }

    carregarClientes();
    carregarChamados();

    const form = document.getElementById("osForm");

    if (form) {
        form.addEventListener("submit", criarChamado);
    }

    const search = document.getElementById("searchInput");

    if (search) {
        search.addEventListener("keyup", filtrarTabela);
    }
});

/* ==========================
   LOGIN TEST (opcional)
========================== */
async function fazerLogin() {

    const resposta = await fetch(`${API_URL}/auth/login`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            username: "admin",
            password: "123"
        })
    });

    const dados = await resposta.json();

    localStorage.setItem("token", dados.token);
    localStorage.setItem("perfil", dados.perfil);

    console.log("LOGIN OK:", dados);
}

/* ==========================
   CLIENTES
========================== */
async function carregarClientes() {

    const token = localStorage.getItem("token");

    try {
        const response = await fetch(`${API_URL}/clientes`, {
            headers: {
                "Authorization": "Bearer " + token
            }
        });

        if (!response.ok) {
            throw new Error("Erro ao carregar clientes");
        }

        const clientes = await response.json();

        const select = document.getElementById("cliente");

        if (!select) return;

        let html = '<option value="">Selecione um cliente</option>';

        clientes.forEach(cliente => {
            html += `
                <option value="${cliente.id}">
                    ${cliente.nome ?? "Sem Nome"}
                </option>
            `;
        });

        select.innerHTML = html;

    } catch (error) {
        console.error(error);
    }
}

/* ==========================
   DASHBOARD
========================== */
function atualizarDashboard(chamados) {

    const total = chamados.length;
   const abertos = chamados.filter(c =>
    c.status?.toUpperCase() === "ABERTO"
).length;

const finalizados = chamados.filter(c =>
    c.status?.toUpperCase() === "FINALIZADO"
).length;

const atrasados = chamados.filter(c =>
    c.status?.toUpperCase() === "ATRASADO"
).length;
    const totalOS = document.getElementById("totalOS");
    const abertosOS = document.getElementById("abertos");
    const finalizadosOS = document.getElementById("finalizados");
    const atrasadasOS = document.getElementById("atrasadas");

    if (totalOS) totalOS.textContent = total;
    if (abertosOS) abertosOS.textContent = abertos;
    if (finalizadosOS) finalizadosOS.textContent = finalizados;
    if (atrasadasOS) atrasadasOS.textContent = atrasados;
}

/* ==========================
   CHAMADOS
========================== */
async function carregarChamados() {

    const token = localStorage.getItem("token");

    try {
        const response = await fetch(`${API_URL}/chamados`, {
            headers: {
                "Authorization": "Bearer " + token
            }
        });

        if (!response.ok) {
            throw new Error("Erro ao carregar chamados");
        }

        const chamados = await response.json();

        atualizarDashboard(chamados);

        const tbody = document.getElementById("tableBody");

        if (!tbody) return;

        let html = "";

        chamados.forEach(chamado => {

            const clienteNome =
                chamado.cliente?.nome ||
                chamado.cliente?.username ||
                "Sem Cliente";

            html += `
                <tr>
                    <td>${chamado.id}</td>
                    <td>${clienteNome}</td>
                    <td>${chamado.descricao ?? ""}</td>
                    <td>${chamado.data ?? ""}</td>
                    <td>${chamado.status ?? ""}</td>
                   <td>
    <button onclick="finalizarChamado(${chamado.id})">Finalizar</button>
    <button onclick="excluirChamado(${chamado.id})">Excluir</button>
</td>
                </tr>
            `;
        });

        tbody.innerHTML = html;

    } catch (error) {
        console.error(error);
    }
}
window.logout = function() {

    localStorage.removeItem("token");
    localStorage.removeItem("perfil");
    localStorage.removeItem("empresaId");

    window.location.href = "login.html";
};
window.abrirTela = async function(nomeTela) {

    document.querySelectorAll("section")
        .forEach(sec => sec.style.display = "none");

    document.getElementById(nomeTela)
        .style.display = "block";

    if(nomeTela === "clientes"){
        await carregarClientes();
    }

    if(nomeTela === "ordens"){
        await carregarChamados();
    }
};

/* ==========================
   CRIAR CHAMADO
========================== */
async function criarChamado(event) {

    event.preventDefault();

    const token = localStorage.getItem("token");

    const clienteId = document.getElementById("cliente").value;
    const descricao = document.getElementById("descricao").value;

    const chamado = {
        descricao,
        valor: 150.0,
        pago: false,
        cliente: {
            id: Number(clienteId)
        }
    };

    try {
        const response = await fetch(`${API_URL}/chamados`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Authorization": "Bearer " + token
            },
            body: JSON.stringify(chamado)
        });

        if (!response.ok) {
            throw new Error("Erro ao criar chamado");
        }

        document.getElementById("osForm").reset();

        carregarChamados();

        alert("Chamado criado com sucesso!");

    } catch (error) {
        console.error(error);
    }
}

/* ==========================
   FINALIZAR
========================== */
async function finalizarChamado(id) {

    const token = localStorage.getItem("token");

    try {
        const response = await fetch(`${API_URL}/chamados/${id}/finalizar`, {
            method: "PUT",
            headers: {
                "Authorization": "Bearer " + token
            }
        });

        if (!response.ok) {
            throw new Error("Erro ao finalizar");
        }

        carregarChamados();

    } catch (error) {
        console.error(error);
    }
}

/* ==========================
   EXCLUIR
========================== */
window.excluirChamado = async function (id) {

    const token = localStorage.getItem("token");

    const response = await fetch(`${API_URL}/chamados/${id}`, {
        method: "DELETE",
        headers: {
            "Authorization": "Bearer " + token
        }
    });

    console.log("DELETE STATUS:", response.status);

    if (!response.ok) {
        alert("Erro ao excluir");
        return;
    }

    carregarChamados();
};
/* ==========================
   FILTRO TABELA
========================== */
function filtrarTabela() {

    const texto = this.value.toLowerCase();

    const linhas = document.querySelectorAll("#tableBody tr");

    linhas.forEach(linha => {

        linha.style.display =
            linha.innerText.toLowerCase().includes(texto)
                ? ""
                : "none";
    });



}   
window.abrirTela = async function(tela) {

    document.querySelectorAll(".pagina")
        .forEach(sec => {
            sec.style.display = "none";
        });

    document.getElementById(tela)
        .style.display = "block";

    const titulo =
        document.getElementById("tituloPagina");

    if (titulo) {

        const nomes = {
            dashboard: "Dashboard",
            novaOs: "Nova Ordem de Serviço",
            ordens: "Ordens de Serviço",
            clientes: "Clientes",
            config: "Configurações"
        };

        titulo.textContent = nomes[tela];
    }

    if (tela === "ordens") {
        await carregarChamados();
    }

    if (tela === "novaOs") {
        await carregarClientes();
    }

    if (tela === "clientes") {
        await carregarListaClientes();
    }
};


window.logout = function() {

    localStorage.clear();

    window.location.href = "login.html";
};
async function carregarListaClientes() {

    const token = localStorage.getItem("token");

    try {

        const response = await fetch(
            `${API_URL}/clientes`,
            {
                headers: {
                    "Authorization": "Bearer " + token
                }
            }
        );

        const clientes = await response.json();

        const tbody =
            document.getElementById("clientesTableBody");

        if (!tbody) return;

        tbody.innerHTML = "";

        clientes.forEach(cliente => {

            tbody.innerHTML += `
                <tr>
                    <td>${cliente.id}</td>
                    <td>${cliente.nome ?? ""}</td>
                    <td>${cliente.telefone ?? ""}</td>
                </tr>
            `;
        });

    } catch (error) {

        console.error(error);

        alert("Erro ao carregar clientes");
    }
}