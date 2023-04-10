# eVibbra WEB

Este projeto foi desenvolvido para gerenciar despesas, categorias de despesas e lançamento de notas fiscais de empresas no sistema eVibbra. Ele proporciona uma interface amigável para usuários lançarem e visualizarem suas despesas, além de monitorar o limite anual de faturamento do MEI.

## Funcionalidades Principais
- Gerenciamento de Despesas: Permite o cadastro, edição e exclusão de despesas com base em categorias específicas.
- Lançamento de Notas Fiscais: Facilita o registro e acompanhamento de notas fiscais emitidas por empresas, categorizadas por tipo de despesa.
- Relatórios de Faturamento: O sistema fornece relatórios que ajudam a acompanhar o total faturado em comparação com o limite anual do MEI.
- Alertas e Notificações: Notificações são geradas automaticamente com base no limite de faturamento, incluindo alertas quando o usuário se aproxima de 80% do limite anual.
- Painel de Empresas: Gestão das empresas cadastradas para associar despesas e notas fiscais.

## Pré-requisitos

Antes de começar a trabalhar com o projeto, certifique-se de que seu ambiente atenda aos seguintes requisitos:

- Node.js: Versão 18.16.1 ou superior
- npm: Versão 9.5.1 ou superior
- Angular CLI: Versão 16.1.4 ou superior

## Configuração do Ambiente

Certifique-se de seguir estas etapas para configurar seu ambiente antes de executar o projeto:

1. **Instale o Node.js e o npm:** Se você não tiver o Node.js e o npm instalados, faça o download e instale-os a partir do site oficial (https://nodejs.org/).

2. **Instale o Angular CLI:** Abra o terminal e execute o seguinte comando para instalar o Angular CLI globalmente:

   ```bash
   npm install -g @angular/cli@16.1.4
   ```

3. Na raiz do projeto **Instale as dependências:** Execute o seguinte comando para instalar as dependências do projeto:

   ```bash
   npm install
   ```
* Nota sobre a Biblioteca PrimeNG: Este projeto utiliza a biblioteca PrimeNG na versão 17.12.0. Observamos que houve uma atualização dessa biblioteca, porém, atualmente não recomendamos a migração para versões mais recentes. Isso se deve a alguns problemas identificados na utilização do componente p-table conforme foi implementado no projeto atual. Em versões mais recentes, como a 17.13.0, certas funcionalidades, como "expandir tudo" e "selecionar tudo", podem não ter suporte adequado. Estamos monitorando ativamente as atualizações e trabalhando para resolver esses problemas, mas, por enquanto, é aconselhável permanecer na versão atual para garantir a estabilidade e o funcionamento correto do projeto. Para mais informações sobre a biblioteca PrimeNG, consulte a documentação oficial em PrimeNG.

## Execução do Projeto

Agora que seu ambiente está configurado e as dependências estão instaladas, você pode iniciar o projeto. Use o seguinte comando:

```bash
ng serve
```
O projeto estará acessível no endereço padrão: http://localhost:4200.

## Módulos e Funcionalidades

O sistema é dividido em vários módulos principais, descritos abaixo:

1. Módulo de Despesas
- Permite o cadastro e visualização de despesas. Cada despesa é associada a uma categoria e a uma empresa cadastrada no sistema.
- Suporta a filtragem de despesas por data, categoria e empresa.
2. Módulo de Notas Fiscais
- Oferece uma interface para cadastrar e gerenciar notas fiscais associadas a despesas.
- Inclui a funcionalidade de upload de arquivos de notas fiscais.
- As notas fiscais são categorizadas para facilitar o acompanhamento de despesas.
3. Módulo de Categorias
- Facilita o gerenciamento de categorias de despesas, permitindo a criação, edição e exclusão de categorias.
- As categorias ajudam a organizar as despesas de acordo com os tipos definidos pelo usuário.
4. Módulo de Empresas
- Gerencia o cadastro de empresas às quais as despesas estão vinculadas.
- As empresas são associadas a notas fiscais e despesas, ajudando no controle do faturamento por empresa.
5. Painel de Controle
- Exibe um dashboard com relatórios de faturamento anual, despesas totais e limites atingidos.
- Inclui alertas quando o usuário atinge 80% do limite de faturamento do MEI, e um resumo mensal das despesas.
6. Notificações
- O sistema envia notificações automáticas baseadas no limite de faturamento, incluindo alertas quando o usuário se aproxima do limite anual.
- Notificações são enviadas mensalmente, no primeiro dia de cada mês, informando o limite disponível do MEI.

## Acesso ao Painel Administrativo
O painel administrativo pode ser acessado pela URL:
```bash
https://evibbra-web/#/login
```
Insira suas credenciais para acessar as funcionalidades administrativas.