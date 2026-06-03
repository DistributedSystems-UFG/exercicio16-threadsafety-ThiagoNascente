Análise das Classes: Vantagens e Limitações
1. SynchronizedRGB (Sincronização Baseada em Bloqueio)
Embora a classe use a palavra-chave synchronized, ela NÃO é totalmente thread-safe em cenários do mundo real sem cuidados extras do cliente.

Vantagens:

a - Mutabilidade: Permite alterar a cor do mesmo objeto sem a necessidade de criar novas instâncias na memória (set e invert).

Limitações e Problemas de Thread Safety:

a - Estado Inconsistente por Fragmentação de Leituras: Os métodos getRGB() e getName() são sincronizados individualmente. Se uma thread chamar getRGB() e, logo em seguida, outra 
thread modificar o objeto via set(), quando a primeira thread chamar getName(), ela pegará o nome da nova cor. Isso gera uma inconsistência de estado (o RGB não condiz com o Nome).

b - Gargalo de Performance (Contenção): Múltiplas threads tentando ler ou modificar o mesmo objeto serão bloqueadas pelo lock do objeto (this), diminuindo o paralelismo.

2. ImmutableRGB (Imutabilidade)
Esta classe é 100% thread-safe por design. Como seu estado não pode ser alterado após a construção, nenhuma thread consegue corromper os dados ou ver um estado parcial/inconsistente.

Vantagens:

a - Thread Safety Absoluto: Não requer travas (locks), eliminando o risco de deadlocks ou condições de corrida de leitura/escrita.

b - **Consistência Garantida**: Ao ler o RGB e o Nome, você tem a certeza absoluta de que eles pertencem ao mesmo estado inicial do objeto.

c - Compartilhamento Seguro: Pode ser compartilhado livremente entre threads sem medo.

Limitações:

Custo de Memória: Qualquer alteração (como o método invert()) exige a criação de um novo objeto na memória (return new ImmutableRGB(...)). Se as mudanças forem de alta frequência, 
isso pode sobrecarregar o Garbage Collector (GC).
