# INF1304 (TL;DR)
## Distribuição e Concorrência

[![License: MIT](https://img.shields.io/badge/license-MIT-green.svg)](https://opensource.org/licenses/MIT)
![Contributors](https://img.shields.io/github/contributors/AlexandreMeslin/INF1304)
![Open Issues](https://img.shields.io/github/issues/AlexandreMeslin/INF1304)
![Open PRs](https://img.shields.io/github/issues-pr/AlexandreMeslin/INF1304)
![PRs Welcome](https://img.shields.io/badge/PRs-welcome-brightgreen.svg)

![Java](https://img.shields.io/badge/language-Java-red.svg)
![C](https://img.shields.io/badge/language-C-blue.svg)
![Shell](https://img.shields.io/badge/language-Shell-green.svg)
![Python](https://img.shields.io/badge/language-Python-yellow.svg)
![HTML](https://img.shields.io/badge/language-HTML-brown.svg)
![Top Language](https://img.shields.io/github/languages/top/AlexandreMeslin/INF1304)
![Languages Count](https://img.shields.io/github/languages/count/AlexandreMeslin/INF1304)
![GitHub commits since tagged version](https://img.shields.io/github/commits-since/AlexandreMeslin/INF1304/v2025.2)

![Last Commit](https://img.shields.io/github/last-commit/AlexandreMeslin/INF1304)
![Repo Size](https://img.shields.io/github/repo-size/AlexandreMeslin/INF1304)
![Code Size](https://img.shields.io/github/languages/code-size/AlexandreMeslin/INF1304)

![GitHub stars](https://img.shields.io/github/stars/AlexandreMeslin/INF1304?style=social)
![GitHub forks](https://img.shields.io/github/forks/AlexandreMeslin/INF1304?style=social)
![GitHub followers](https://img.shields.io/github/followers/AlexandreMeslin)
![GitHub User's stars](https://img.shields.io/github/stars/AlexandreMeslin)
![GitHub watchers](https://img.shields.io/github/watchers/AlexandreMeslin/INF1304)

**Frameworks & Tools:**  
![Django](https://img.shields.io/badge/Django-000000?style=flat-square&logo=django&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-2D3748?style=flat-square&logo=mysql&logoColor=white)
![AWS](https://img.shields.io/badge/AWS-FF9900?style=flat-square&logo=amazonaws&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-4169E1?style=flat-square&logo=postgresql&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2496ED?style=flat-square&logo=docker&logoColor=white)
![Git](https://img.shields.io/badge/Git-F05033?style=flat-square&logo=git&logoColor=white)

Este repositório contém todo o código e material relacionado ao curso INF1304 - Distribuição e Concorrência.
Ele inclui exemplos, exercícios e projetos que cobrem vários aspectos de programção distribuída e concorrente.

## Estrutura do Repositório

- `Programas/`: Contém os códigos-fonte dos programas desenvolvidos durante o curso, organizados por tópicos.
- `Exercicios/`: Contém os exercícios propostos durante as aulas, com soluções
- `Projetos/`: Contém os projetos finais e trabalhos práticos realizados pelos alunos.
- `Documentacao/`: Contém documentos, slides e materiais de referência utilizados durante o curso
- `README.md`: Este arquivo, que fornece uma visão geral do repositório e seu conteúdo.

## Contribuição

Contribuições são bem-vindas! 
Se você deseja contribuir com este repositório, por favor, siga as diretrizes abaixo:
1. Fork este repositório
2. Crie uma branch para sua contribuição (`git checkout -b minha-contribuicao`)
3. Faça suas alterações e commit (`git commit -m 'Minha contribuição'`)
4. Push para a branch (`git push origin minha-contribuicao`)
5. Abra um Pull Request

## Licença

Este projeto está licenciado sob a Creative Commons Legal Code.
Veja o arquivo [LICENSE](LICENSE) para mais detalhes.

# Dicas Importantes (a medida que você as use)

## Documentação do seu programa

Use Doxygen (estilo JavaDoc) para documentar o seu código

Para extrair a documentação, siga os seguintes passos:

1. Instale o Doxygen (https://www.doxygen.nl/download.html)

   `sudo apt install doxygen graphviz`

1. Crie um arquivo de configuração Doxyfile:

    `doxygen -g Doxyfile`

1. Edite o arquivo `Doxyfile` para configurar:

    - `PROJECT_NAME = "Nome do Projeto"`
    - `OUTPUT_DIRECTORY = docs`
    - `GENERATE_LATEX = NO` (se precisar, troque para `YES`)
    - `GENERATE_HTML = YES`
    - `RECURSIVE = YES`
    - `OPTIMIZE_OUTPUT_FOR_C = YES`
    - `JAVADOC_AUTOBRIEF = YES`
