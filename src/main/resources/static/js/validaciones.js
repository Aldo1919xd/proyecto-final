document.addEventListener('DOMContentLoaded', function() {

    function mostrarError(input, mensaje){
        var container = input.closest('.mb-3','.col');
        if(!container) return;
        var feedback = container.querySelector('.invalid-feedback');
        input.classList.add('is-invalid');
        if (feedback) {
            feedback.textContent = mensaje;
        }
    }

    function limpiarError(input){
        input.classList.remove('is-invalid');
        var container = input.closest('.mb-3','.col');
        if(!container) return;
        var feedback = container.querySelector('.invalid-feedback');
        if(feedback) {
            feedback.textContent = '';
        }
    }

    function validarRequerido(input){
        if(input.hasAttribute('required') && (!input.value || input.value.trim() === '')){
            mostrarError(input, 'Este campo es obligatorio');
            return false;
        }
        limpiarError(input);
        return true;
    }

})