public class MainSynchronized {

    public static void main(String[] args) throws InterruptedException {
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
                    System.out.println("Inconsistência! RGB: " + rgb + " | Nome: " + nome);
                    break;
                }
            }
        });

        escritorSynch.start();
        leitorSynch.start();
        escritorSynch.join();
        leitorSynch.join();
    }
}