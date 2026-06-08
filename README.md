**Classe SynchronizedRGB** (Sincronização Baseada em Bloqueio)
Embora a classe use a palavra-chave synchronized, ela NÃO é totalmente thread-safe.

Vantagens:

Alta mutabilidade, permitindo alterar a cor do mesmo objeto sem a necessidade de criar novas instâncias na memória (set e invert).

Limitações:

Pode apresentar estado inconsistente por fragmentação de leituras, Os métodos getRGB() e getName() são sincronizados individualmente. Se uma thread chamar getRGB() e, logo em seguida, outra thread modificar o objeto via set(), quando a primeira thread chamar getName(), ela pegará o nome da nova cor. Isso gera uma inconsistência de estado. Além disso, apresenta gargalo de performance (Contenção), no cenário de múltiplas threads tentando ler ou modificar o mesmo objeto serão bloqueadas pelo lock do objeto (this), diminuindo o paralelismo.

**Classe ImmutableRGB** (Imutabilidade)
Esta classe é 100% thread-safe por design. Como seu estado não pode ser alterado após a construção, nenhuma thread consegue corromper os dados ou ver um estado parcial/inconsistente.

Vantagens:

É Thread safety absoluto, ou seja, não requer travas (locks), eliminando o risco de deadlocks ou condições de corrida de leitura/escrita; Apresenta consistência garantida, pois ao ler o RGB e o Nome, temos a certeza de que eles pertencem ao mesmo estado inicial do objeto; Preserva um compartilhamento seguro, podendo ser compartilhado livremente entre threads.

Limitações:

É mais custoso por memória, qualquer alteração (como o método invert()) exige a criação de um novo objeto na memória (return new ImmutableRGB(...)). Se as mudanças forem frequentes, isso pode sobrecarregar o Garbage Collector (GC) da JVM.
