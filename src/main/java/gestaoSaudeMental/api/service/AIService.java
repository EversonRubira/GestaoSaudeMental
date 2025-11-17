package gestaoSaudeMental.api.service;

public interface AIService {

    /**
     * Gera uma mensagem motivacional personalizada usando IA
     *
     * @param nome Nome do usuário
     * @param estadoEmocional Estado emocional atual
     * @param atividade Atividade realizada
     * @return Mensagem motivacional gerada pela IA
     */
    String gerarMensagemMotivacional(String nome, String estadoEmocional, String atividade);
}
