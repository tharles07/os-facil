/* ==================== Variáveis Globais ==================== */
const app = {
    orders: [],
    currentOrder: null,
};

/* ==================== Inicialização ==================== */
document.addEventListener('DOMContentLoaded', function() {
    initializeApp();
    attachEventListeners();
});

function initializeApp() {
    // Carregar dados do localStorage
    const savedOrders = localStorage.getItem('orders');
    if (savedOrders) {
        app.orders = JSON.parse(savedOrders);
    }
    
    console.log('Aplicação iniciada com sucesso!');
}

/* ==================== Event Listeners ==================== */
function attachEventListeners() {
    // Form submit
    const form = document.getElementById('osForm');
    if (form) {
        form.addEventListener('submit', handleFormSubmit);
    }

    // Search input
    const searchInput = document.getElementById('searchInput');
    if (searchInput) {
        searchInput.addEventListener('keyup', handleSearch);
    }

    // Menu navigation
    const menuItems = document.querySelectorAll('.menu-item');
    menuItems.forEach(item => {
        item.addEventListener('click', handleMenuClick);
    });

    // Nav links
    const navLinks = document.querySelectorAll('.nav-link');
    navLinks.forEach(link => {
        link.addEventListener('click', handleNavClick);
    });

    // Delete buttons
    attachDeleteButtonListeners();
    
    // Edit buttons
    attachEditButtonListeners();
}

/* ==================== Form Handlers ==================== */
function handleFormSubmit(e) {
    e.preventDefault();

    const cliente = document.getElementById('cliente').value;
    const descricao = document.getElementById('descricao').value;
    const data = document.getElementById('data').value;
    const status = document.getElementById('status').value;

    if (!cliente || !descricao || !data || !status) {
        showNotification('Por favor, preencha todos os campos!', 'error');
        return;
    }

    const newOrder = {
        id: generateId(),
        cliente,
        descricao,
        data: formatDate(data),
        status: getStatusText(status),
        createdAt: new Date().toISOString()
    };

    app.orders.push(newOrder);
    saveOrders();
    addRowToTable(newOrder);

    // Limpar formulário
    document.getElementById('osForm').reset();
    
    showNotification('Ordem de serviço criada com sucesso!', 'success');
    
    // Atualizar estatísticas
    updateStats();
}

function handleSearch(e) {
    const searchTerm = e.target.value.toLowerCase();
    const tableRows = document.querySelectorAll('#tableBody tr');

    tableRows.forEach(row => {
        const text = row.innerText.toLowerCase();
        row.style.display = text.includes(searchTerm) ? '' : 'none';
    });
}

/* ==================== Table Handlers ==================== */
function addRowToTable(order) {
    const tableBody = document.getElementById('tableBody');
    
    const statusClass = getStatusClass(order.status);
    
    const row = document.createElement('tr');
    row.innerHTML = `
        <td>${order.id}</td>
        <td>${order.cliente}</td>
        <td>${order.descricao}</td>
        <td>${order.data}</td>
        <td><span class="badge ${statusClass}">${order.status}</span></td>
        <td>
            <button class="btn-small btn-edit" onclick="handleEdit('${order.id}')">✏️ Editar</button>
            <button class="btn-small btn-delete" onclick="handleDelete('${order.id}')">🗑️ Deletar</button>
        </td>
    `;
    
    tableBody.prepend(row);
}

function attachDeleteButtonListeners() {
    const deleteButtons = document.querySelectorAll('.btn-delete');
    deleteButtons.forEach(btn => {
        btn.addEventListener('click', function(e) {
            e.stopPropagation();
        });
    });
}

function attachEditButtonListeners() {
    const editButtons = document.querySelectorAll('.btn-edit');
    editButtons.forEach(btn => {
        btn.addEventListener('click', function(e) {
            e.stopPropagation();
        });
    });
}

function handleDelete(id) {
    if (confirm('Tem certeza que deseja deletar esta ordem de serviço?')) {
        app.orders = app.orders.filter(order => order.id !== id);
        saveOrders();
        
        // Remover linha da tabela
        const rows = document.querySelectorAll('#tableBody tr');
        rows.forEach(row => {
            if (row.innerText.includes(id)) {
                row.remove();
            }
        });
        
        showNotification('Ordem de serviço deletada com sucesso!', 'success');
        updateStats();
    }
}

function handleEdit(id) {
    const order = app.orders.find(o => o.id === id);
    
    if (order) {
        document.getElementById('cliente').value = order.cliente;
        document.getElementById('descricao').value = order.descricao;
        document.getElementById('data').value = order.data;
        document.getElementById('status').value = order.status.toLowerCase();
        
        app.currentOrder = id;
        
        // Scroll para o formulário
        document.querySelector('.form-section').scrollIntoView({ behavior: 'smooth' });
        
        showNotification(`Editando ordem ${id}`, 'info');
    }
}

/* ==================== Menu Handlers ==================== */
function handleMenuClick(e) {
    e.preventDefault();
    
    // Remover active de todos os itens
    document.querySelectorAll('.menu-item').forEach(item => {
        item.classList.remove('active');
    });
    
    // Adicionar active ao item clicado
    e.target.classList.add('active');
    
    const action = e.target.textContent.trim();
    console.log('Menu item clicado:', action);
}

function handleNavClick(e) {
    e.preventDefault();
    
    // Remover active de todos os links
    document.querySelectorAll('.nav-link').forEach(link => {
        link.classList.remove('active');
    });
    
    // Adicionar active ao link clicado
    e.target.classList.add('active');
    
    const section = e.target.getAttribute('href');
    console.log('Navegação para:', section);
}

/* ==================== Utility Functions ==================== */
function generateId() {
    const timestamp = Date.now().toString(36);
    const randomStr = Math.random().toString(36).substr(2, 9);
    return `#${(parseInt(timestamp + randomStr, 36) % 10000).toString().padStart(4, '0')}`;
}

function formatDate(dateString) {
    const date = new Date(dateString);
    return date.toLocaleDateString('pt-BR');
}

function getStatusText(status) {
    const statusMap = {
        'pending': 'Pendente',
        'in-progress': 'Em Andamento',
        'completed': 'Concluída',
        'delayed': 'Atrasada'
    };
    return statusMap[status] || 'Pendente';
}

function getStatusClass(status) {
    const classMap = {
        'Pendente': 'badge-pending',
        'Em Andamento': 'badge-in-progress',
        'Concluída': 'badge-completed',
        'Atrasada': 'badge-delayed'
    };
    return classMap[status] || 'badge-pending';
}

function showNotification(message, type = 'info') {
    // Criar notificação
    const notification = document.createElement('div');
    notification.className = `notification notification-${type}`;
    notification.textContent = message;
    notification.style.cssText = `
        position: fixed;
        top: 20px;
        right: 20px;
        background-color: ${type === 'success' ? '#10b981' : type === 'error' ? '#ef4444' : '#3b82f6'};
        color: white;
        padding: 1rem 1.5rem;
        border-radius: 0.5rem;
        box-shadow: 0 10px 15px rgba(0, 0, 0, 0.1);
        z-index: 1000;
        animation: slideIn 0.3s ease;
    `;
    
    document.body.appendChild(notification);
    
    // Remover notificação após 3 segundos
    setTimeout(() => {
        notification.style.animation = 'slideOut 0.3s ease';
        setTimeout(() => notification.remove(), 300);
    }, 3000);
}

function updateStats() {
    const stats = {
        total: app.orders.length,
        pending: app.orders.filter(o => o.status === 'Pendente').length,
        inProgress: app.orders.filter(o => o.status === 'Em Andamento').length,
        completed: app.orders.filter(o => o.status === 'Concluída').length,
        delayed: app.orders.filter(o => o.status === 'Atrasada').length
    };
    
    // Atualizar cards de estatísticas (se existirem)
    console.log('Estatísticas atualizadas:', stats);
}

function saveOrders() {
    localStorage.setItem('orders', JSON.stringify(app.orders));
    console.log('Dados salvos no localStorage');
}

/* ==================== Animações CSS ==================== */
const style = document.createElement('style');
style.textContent = `
    @keyframes slideIn {
        from {
            transform: translateX(400px);
            opacity: 0;
        }
        to {
            transform: translateX(0);
            opacity: 1;
        }
    }
    
    @keyframes slideOut {
        from {
            transform: translateX(0);
            opacity: 1;
        }
        to {
            transform: translateX(400px);
            opacity: 0;
        }
    }
    
    @keyframes fadeIn {
        from {
            opacity: 0;
        }
        to {
            opacity: 1;
        }
    }
`;
document.head.appendChild(style);

/* ==================== Logs ==================== */
console.log('%c✨ OS Fácil - Frontend Carregado com Sucesso! ✨', 'color: #2563eb; font-size: 16px; font-weight: bold;');
console.log('Versão: 1.0.0');
console.log('Desenvolvido com ❤️');
