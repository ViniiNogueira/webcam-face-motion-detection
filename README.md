# WebCam Java: detecção de movimento e rosto em tempo real

Projeto de estudo em Java que exibe o vídeo da webcam em uma janela, avisa na tela quando detecta movimento e desenha um quadrado verde em volta dos rostos encontrados.

## Funcionalidades

- Exibição do vídeo da webcam em tempo real (com espelhamento e contador de FPS)
- Detecção de movimento com aviso visual na tela (rótulo vermelho) e mensagem no console
- Detecção de rosto com quadrado desenhado sobre o vídeo
- Detecção de rosto rodando em thread separada, sem travar a interface

## Tecnologias

| Tecnologia | Uso |
|---|---|
| Java 21 | Linguagem |
| Maven | Gerenciamento de dependências |
| [webcam-capture](https://github.com/sarxos/webcam-capture) (Sarxos) 0.3.12 | Captura de frames e detecção de movimento |
| [OpenCV](https://opencv.org/) via OpenPnP 4.9.0-0 | Detecção de rosto com Haar Cascade |
| Java Swing | Interface gráfica |

## Estrutura do projeto

Separacao por pacotes:
```
UI:
  Tela
motion:
  DetectorFace
  DetectorMovimento
Main
```

## Como funciona

O pacote **UI** cuida da interface gráfica e de tudo que está relacionado a ela, ou seja, a classe Tela recebe a webcam e exibe o vídeo em tempo real.
Ela também mostra o aviso de movimento e desenha os quadrados sobre os rostos.

O pacote **motion** cuida da detecção de movimento: <br>
A classe DetectorMovimento compara frames consecutivos da câmera a cada 500 ms e,quando encontra diferença, executa a ação recebida no construtor. <br>
A classe DetectorFace cuida da detecção de rosto. A classe roda em uma thread própria, para não travar a interface. A cada rodada ela pega o frame mais recente da câmera, converte para escala de cinza e usa o OpenCV com o classificador Haar Cascade (`haarcascade_frontalface_default.xml`) para procurar rostos. Os rostos encontrados são entregues, como uma lista de retângulos, à ação recebida no construtor, e a Tela usa essa lista para desenhar os quadrados.

## Pré-requisitos

- JDK 21 ou superior
- Maven
- Uma webcam funcionando e não ocupada por outro programa

## Como executar

1. Clone o repositório:

   ```bash
   git clone https://github.com/ViniiNogueira/webcam-face-motion-detection.git
   ```

2. Baixe o classificador de rosto e coloque o arquivo na **raiz do projeto** :

   [haarcascade_frontalface_default.xml](https://raw.githubusercontent.com/opencv/opencv/master/data/haarcascades/haarcascade_frontalface_default.xml)

   O nome do arquivo precisa ser exatamente `haarcascade_frontalface_default.xml`.

3. Compile e execute a classe `org.example.Main`, pela IDE ou pelo Maven:

   ```bash
   mvn compile
   mvn exec:java -Dexec.mainClass="org.example.Main"
   ```

   O comando `exec:java` exige o plugin `exec-maven-plugin`. Se preferir, rode a `Main` direto pela IDE.

## Observações e limitações

- O classificador Haar Cascade usado é o frontal: detecta rostos de frente e perde o rosto quando a pessoa vira muito de lado.
- O programa é capaz de detectar mais de 1 rosto simultaneamente
- A detecção depende de boa iluminação. Com pouca luz ou contra a luz, pode não encontrar nada.
- Se o caminho do arquivo .xml estiver errado, o OpenCV não gera erro: o classificador apenas carrega vazio e nunca detecta rosto. O código avisa no console quando isso acontece através dessa verificação:  
- ```
  if (classificador.empty()) {
            System.out.println("Não carregou o arquivo do classificador!");
        }
  ```
- A mensagem `SLF4J: Failed to load class "org.slf4j.impl.StaticLoggerBinder"` não é erro. Eu apenas não adicionei ele ao projeto, para removê-la, adicione a dependência `slf4j-simple` ao `pom.xml`.

<!--
## Próximos passos

- Ajustar a sensibilidade da detecção (parâmetros do `detectMultiScale` e do detector de movimento)
- Salvar uma foto com data e hora quando houver detecção
- Testar outros classificadores (olhos, sorriso) e outros tipos de objeto
- Enviar os eventos detectados para uma API em Spring Boot e guardar o histórico em banco de dados

## O que pratiquei neste projeto

- Threads e visibilidade de dados entre threads (`volatile`)
- Callbacks com `Runnable` e `Consumer`, lambdas e classes anônimas
- Separação de responsabilidades e injeção da dependência pelo construtor
- Integração de bibliotecas externas, inclusive com código nativo (OpenCV)
- Interface gráfica com Swing e desenho customizado -->

