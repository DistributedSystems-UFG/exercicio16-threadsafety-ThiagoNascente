public class Main {

    public static void main(String[] args) throws InterruptedException {
        // --- 1. TESTE DA CLASSE MUTÁVEL (SynchronizedRGB) ---
        System.out.println("--- Testando SynchronizedRGB (Pode gerar inconsistência) ---");
        SynchronizedRGB synchColor = new SynchronizedRGB(0, 0, 0, "Preto");
        int num = 2_000_000;

        // Thread que fica alternando a cor
        Thread escritorSynch = new Thread(() -> {
            for (int i = 0; i < num; i++) {
                synchColor.set(0, 0, 0, "Preto");
                synchColor.set(255, 255, 255, "Branco");
            }
        });

        // Thread que lê o RGB e o Nome de forma fragmentada
        Thread leitorSynch = new Thread(() -> {
            for (int i = 0; i < num; i++) {
                int rgb = synchColor.getRGB();
                String nome = synchColor.getName();
                
                // Se o RGB for 0 (Preto) mas o nome for Branco (ou vice-versa), pegamos o bug!
                if ((rgb == 0 && "Branco".equals(nome)) || (rgb != 0 && "Preto".equals(nome))) {
                    System.out.println("❌ Inconsistência! RGB: " + rgb + " | Nome: " + nome);
                    break;
                }
            }
        });

        escritorSynch.start();
        leitorSynch.start();
        escritorSynch.join();
        leitorSynch.join();


        // --- 2. TESTE DA CLASSE IMUTÁVEL (ImmutableRGB) ---
        System.out.println("\n--- Testando ImmutableRGB (Sempre Segura) ---");
        // Usamos um array de 1 posição apenas para poder trocar a referência entre as threads
        final ImmutableRGB[] container = { new ImmutableRGB(0, 0, 0, "Preto") };

        Thread escritorImmutable = new Thread(() -> {
            for (int i = 0; i < num; i++) {
                container[0] = new ImmutableRGB(0, 0, 0, "Preto");
                container[0] = new ImmutableRGB(255, 255, 255, "Branco");
            }
        });

        Thread leitorImmutable = new Thread(() -> {
            for (int i = 0; i < num; i++) {
                // Captura a foto (referência) do objeto naquele exato milissegundo
                ImmutableRGB copiaLocal = container[0];
                int rgb = copiaLocal.getRGB();
                String nome = copiaLocal.getName();

                if ((rgb == 0 && "Branco".equals(nome)) || (rgb != 0 && "Preto".equals(nome))) {
                    System.out.println("❌ Isso nunca vai acontecer!");
                }
            }
            System.out.println("✔️ ImmutableRGB finalizou sem nenhuma quebra de estado.");
        });

        escritorImmutable.start();
        leitorImmutable.start();
        escritorImmutable.join();
        leitorImmutable.join();
    }
}
