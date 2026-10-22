import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main extends JFrame {

    private final Color BG = new Color(13, 18, 30);
    private final Color PANEL = new Color(22, 29, 45);
    private final Color PANEL2 = new Color(29, 38, 58);
    private final Color BLUE = new Color(76, 139, 245);
    private final Color GREEN = new Color(64, 201, 133);
    private final Color TEXT = new Color(238, 242, 250);
    private final Color MUTED = new Color(155, 166, 185);
    private final Color RED = new Color(239, 91, 91);

    private AppData data;

    private JPanel content;
    private JLabel xpLabel;
    private JLabel levelLabel;
    private JLabel streakLabel;

    public Main() {
        data = DataManager.load();

        setTitle("FocusMind");
        setSize(1200, 760);
        setMinimumSize(new Dimension(1000, 650));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        buildInterface();
        showDashboard();
    }

    private void buildInterface() {

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BG);

        root.add(createSidebar(), BorderLayout.WEST);

        content = new JPanel(new BorderLayout());
        content.setBackground(BG);
        content.setBorder(new EmptyBorder(30, 35, 30, 35));

        root.add(content, BorderLayout.CENTER);

        setContentPane(root);
    }

    private JPanel createSidebar() {

        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(220, 0));
        sidebar.setBackground(new Color(10, 14, 24));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(new EmptyBorder(25, 15, 20, 15));

        JLabel logo = new JLabel("FOCUSMIND");
        logo.setForeground(TEXT);
        logo.setFont(new Font("SansSerif", Font.BOLD, 22));
        logo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitle = new JLabel("Entrena tu enfoque");
        subtitle.setForeground(MUTED);
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 12));
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        sidebar.add(logo);
        sidebar.add(Box.createVerticalStrut(4));
        sidebar.add(subtitle);
        sidebar.add(Box.createVerticalStrut(35));

        sidebar.add(navButton("⌂   Inicio", e -> showDashboard()));
        sidebar.add(navButton("🎯   Mis metas", e -> showGoals()));
        sidebar.add(navButton("✓   Tareas de hoy", e -> showTasks()));
        sidebar.add(navButton("◉   Entrenamiento", e -> showTraining()));
        sidebar.add(navButton("◷   Concentración", e -> showFocusTimer()));
        sidebar.add(navButton("▥   Progreso", e -> showProgress()));

        sidebar.add(Box.createVerticalGlue());

        JLabel version = new JLabel("Versión 1.0");
        version.setForeground(new Color(100, 110, 130));
        version.setAlignmentX(Component.LEFT_ALIGNMENT);

        sidebar.add(version);

        return sidebar;
    }

    private JButton navButton(String text, ActionListener action) {

        JButton button = new JButton(text);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        button.setAlignmentX(Component.LEFT_ALIGNMENT);

        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setForeground(TEXT);
        button.setBackground(new Color(10, 14, 24));
        button.setBorder(new EmptyBorder(10, 12, 10, 10));
        button.setFocusPainted(false);
        button.setFont(new Font("SansSerif", Font.PLAIN, 14));

        button.addActionListener(action);

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(PANEL2);
            }

            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(10, 14, 24));
            }
        });

        return button;
    }

    private void showDashboard() {

        content.removeAll();

        JPanel page = new JPanel();
        page.setBackground(BG);
        page.setLayout(new BoxLayout(page, BoxLayout.Y_AXIS));

        JLabel greeting = new JLabel("Hola, " + data.userName + " 👋");
        greeting.setForeground(TEXT);
        greeting.setFont(new Font("SansSerif", Font.BOLD, 32));

        JLabel description = new JLabel("Vamos a convertir tus objetivos en acciones.");
        description.setForeground(MUTED);
        description.setFont(new Font("SansSerif", Font.PLAIN, 15));

        page.add(greeting);
        page.add(Box.createVerticalStrut(5));
        page.add(description);
        page.add(Box.createVerticalStrut(30));

        JPanel stats = new JPanel(new GridLayout(1, 3, 15, 0));
        stats.setOpaque(false);
        stats.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));

        xpLabel = createStatValue("XP", data.xp + " XP");
        levelLabel = createStatValue("Nivel", String.valueOf(data.level));
        streakLabel = createStatValue("Racha", data.streak + " días");

        stats.add(xpLabel.getParent());
        stats.add(levelLabel.getParent());
        stats.add(streakLabel.getParent());

        page.add(stats);
        page.add(Box.createVerticalStrut(25));

        JPanel mainCards = new JPanel(new GridLayout(1, 2, 20, 0));
        mainCards.setOpaque(false);
        mainCards.setMaximumSize(new Dimension(Integer.MAX_VALUE, 240));

        JPanel today = cardPanel();

        JLabel todayTitle = title("🎯 Tu prioridad de hoy");
        today.add(todayTitle, BorderLayout.NORTH);

        String priority = getPriorityTask();

        JLabel priorityLabel = new JLabel(
                "<html><div style='width:330px'><font size='5'>"
                        + priority
                        + "</font><br><br>"
                        + "<font color='#9BA6B9'>Concéntrate en una cosa a la vez.</font>"
                        + "</div></html>"
        );

        priorityLabel.setForeground(TEXT);
        today.add(priorityLabel, BorderLayout.CENTER);

        JButton focus = primaryButton("Comenzar sesión");
        focus.addActionListener(e -> showFocusTimer());

        today.add(focus, BorderLayout.SOUTH);

        JPanel progress = cardPanel();

        progress.add(title("📊 Tu progreso"), BorderLayout.NORTH);

        JPanel progressInside = new JPanel();
        progressInside.setOpaque(false);
        progressInside.setLayout(new BoxLayout(progressInside, BoxLayout.Y_AXIS));

        addProgress(progressInside, "Metas", calculateGoalProgress());
        addProgress(progressInside, "Tareas", calculateTaskProgress());
        addProgress(progressInside, "Entrenamiento", data.trainingScore);

        progress.add(progressInside, BorderLayout.CENTER);

        mainCards.add(today);
        mainCards.add(progress);

        page.add(mainCards);

        page.add(Box.createVerticalStrut(25));

        JPanel motivation = cardPanel();

        JLabel motivationText = new JLabel(
                "<html><font size='5'>"
                        + getMotivation()
                        + "</font></html>"
        );

        motivationText.setForeground(TEXT);
        motivation.add(motivationText, BorderLayout.CENTER);

        page.add(motivation);

        content.add(new JScrollPane(
                page,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        ), BorderLayout.CENTER);

        refresh();
    }

    private JLabel createStatValue(String name, String value) {

        JPanel panel = cardPanel();

        JLabel title = new JLabel(name);
        title.setForeground(MUTED);
        title.setFont(new Font("SansSerif", Font.PLAIN, 13));

        JLabel number = new JLabel(value);
        number.setForeground(TEXT);
        number.setFont(new Font("SansSerif", Font.BOLD, 25));

        panel.add(title, BorderLayout.NORTH);
        panel.add(number, BorderLayout.CENTER);

        return number;
    }

    private void addProgress(JPanel panel, String name, int percentage) {

        JLabel label = new JLabel(name + "    " + percentage + "%");
        label.setForeground(TEXT);
        label.setFont(new Font("SansSerif", Font.PLAIN, 13));

        JProgressBar bar = new JProgressBar(0, 100);
        bar.setValue(percentage);
        bar.setForeground(BLUE);
        bar.setBackground(new Color(45, 53, 70));
        bar.setBorderPainted(false);

        panel.add(label);
        panel.add(bar);
        panel.add(Box.createVerticalStrut(15));
    }

    private void showGoals() {

        content.removeAll();

        JPanel page = basePage();

        JLabel header = pageTitle("Mis metas");
        page.add(header);
        page.add(Box.createVerticalStrut(5));

        JLabel info = muted("Convierte lo que quieres conseguir en pasos concretos.");
        page.add(info);
        page.add(Box.createVerticalStrut(20));

        JButton add = primaryButton("+ Nueva meta");
        add.addActionListener(e -> createGoal());

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        top.add(add, BorderLayout.EAST);

        page.add(top);
        page.add(Box.createVerticalStrut(20));

        if (data.goals.isEmpty()) {

            page.add(emptyPanel(
                    "🎯",
                    "Todavía no tienes metas",
                    "Crea tu primera meta y empieza a construir tu camino."
            ));

        } else {

            for (Goal goal : data.goals) {

                JPanel card = cardPanel();

                JPanel left = new JPanel();
                left.setOpaque(false);
                left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));

                JLabel title = new JLabel(goal.title);
                title.setForeground(TEXT);
                title.setFont(new Font("SansSerif", Font.BOLD, 18));

                JLabel description = new JLabel(goal.description);
                description.setForeground(MUTED);

                JProgressBar bar = new JProgressBar(0, 100);
                bar.setValue(goal.progress);
                bar.setForeground(GREEN);
                bar.setBackground(new Color(45, 53, 70));
                bar.setBorderPainted(false);

                left.add(title);
                left.add(Box.createVerticalStrut(5));
                left.add(description);
                left.add(Box.createVerticalStrut(15));
                left.add(bar);

                JButton increase = secondaryButton("+ Progreso");
                increase.addActionListener(e -> {

                    goal.progress = Math.min(100, goal.progress + 10);

                    if (goal.progress == 100 && !goal.completed) {
                        goal.completed = true;
                        addXP(100);
                        JOptionPane.showMessageDialog(
                                this,
                                "🎉 ¡Meta completada!\nHas ganado 100 XP."
                        );
                    }

                    DataManager.save(data);
                    showGoals();
                });

                JButton delete = secondaryButton("Eliminar");
                delete.addActionListener(e -> {

                    int answer = JOptionPane.showConfirmDialog(
                            this,
                            "¿Eliminar esta meta?",
                            "Confirmación",
                            JOptionPane.YES_NO_OPTION
                    );

                    if (answer == JOptionPane.YES_OPTION) {
                        data.goals.remove(goal);
                        DataManager.save(data);
                        showGoals();
                    }
                });

                JPanel buttons = new JPanel();
                buttons.setOpaque(false);
                buttons.add(increase);
                buttons.add(delete);

                card.add(left, BorderLayout.CENTER);
                card.add(buttons, BorderLayout.EAST);

                page.add(card);
                page.add(Box.createVerticalStrut(12));
            }
        }

        content.add(new JScrollPane(page), BorderLayout.CENTER);

        refresh();
    }

    private void createGoal() {

        JTextField title = new JTextField();
        JTextArea description = new JTextArea(4, 20);

        JPanel panel = new JPanel(new BorderLayout(10, 10));

        JPanel fields = new JPanel();
        fields.setLayout(new BoxLayout(fields, BoxLayout.Y_AXIS));

        fields.add(new JLabel("Nombre de la meta:"));
        fields.add(title);
        fields.add(Box.createVerticalStrut(10));
        fields.add(new JLabel("Descripción:"));
        fields.add(description);

        panel.add(fields);

        int result = JOptionPane.showConfirmDialog(
                this,
                panel,
                "Nueva meta",
                JOptionPane.OK_CANCEL_OPTION
        );

        if (result == JOptionPane.OK_OPTION) {

            if (title.getText().trim().isEmpty()) {
                return;
            }

            data.goals.add(
                    new Goal(
                            title.getText().trim(),
                            description.getText().trim()
                    )
            );

            DataManager.save(data);
            showGoals();
        }
    }

    private void showTasks() {

        content.removeAll();

        JPanel page = basePage();

        page.add(pageTitle("Tareas de hoy"));
        page.add(muted("Pequeñas acciones que te acercan a tus metas."));
        page.add(Box.createVerticalStrut(20));

        JButton add = primaryButton("+ Nueva tarea");
        add.addActionListener(e -> createTask());

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        top.add(add, BorderLayout.EAST);

        page.add(top);
        page.add(Box.createVerticalStrut(20));

        if (data.tasks.isEmpty()) {

            page.add(emptyPanel(
                    "✓",
                    "Tu día está vacío",
                    "Añade una tarea para comenzar."
            ));

        } else {

            for (Task task : data.tasks) {

                JPanel card = cardPanel();

                JCheckBox check = new JCheckBox(task.title);
                check.setSelected(task.completed);
                check.setForeground(task.completed ? MUTED : TEXT);
                check.setBackground(PANEL);
                check.setFont(new Font("SansSerif", Font.BOLD, 16));

                check.addActionListener(e -> {

                    if (check.isSelected() && !task.completed) {
                        task.completed = true;
                        addXP(25);
                    }

                    DataManager.save(data);
                    showTasks();
                });

                JLabel duration = new JLabel(task.minutes + " min");
                duration.setForeground(MUTED);

                card.add(check, BorderLayout.CENTER);
                card.add(duration, BorderLayout.EAST);

                page.add(card);
                page.add(Box.createVerticalStrut(10));
            }
        }

        content.add(new JScrollPane(page), BorderLayout.CENTER);

        refresh();
    }

    private void createTask() {

        JTextField title = new JTextField();

        JSpinner minutes = new JSpinner(
                new SpinnerNumberModel(25, 1, 180, 1)
        );

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.add(new JLabel("Tarea:"));
        panel.add(title);
        panel.add(Box.createVerticalStrut(10));
        panel.add(new JLabel("Duración estimada (minutos):"));
        panel.add(minutes);

        int result = JOptionPane.showConfirmDialog(
                this,
                panel,
                "Nueva tarea",
                JOptionPane.OK_CANCEL_OPTION
        );

        if (result == JOptionPane.OK_OPTION &&
                !title.getText().trim().isEmpty()) {

            data.tasks.add(
                    new Task(
                            title.getText().trim(),
                            (Integer) minutes.getValue()
                    )
            );

            DataManager.save(data);
            showTasks();
        }
    }

    private void showFocusTimer() {

        content.removeAll();

        JPanel page = basePage();

        page.add(pageTitle("Modo concentración"));
        page.add(muted("Una tarea. Un objetivo. Un momento."));
        page.add(Box.createVerticalStrut(40));

        JPanel center = new JPanel();
        center.setOpaque(false);
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));

        JLabel timer = new JLabel("25:00");
        timer.setForeground(TEXT);
        timer.setFont(new Font("SansSerif", Font.BOLD, 80));
        timer.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel status = new JLabel("Preparado para comenzar");
        status.setForeground(MUTED);
        status.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton start = primaryButton("▶ Comenzar");
        start.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton reset = secondaryButton("Reiniciar");
        reset.setAlignmentX(Component.CENTER_ALIGNMENT);

        center.add(timer);
        center.add(Box.createVerticalStrut(10));
        center.add(status);
        center.add(Box.createVerticalStrut(30));
        center.add(start);
        center.add(Box.createVerticalStrut(10));
        center.add(reset);

        page.add(center);

        final int[] seconds = {1500};
        final Timer[] timerObject = new Timer[1];

        timerObject[0] = new Timer(1000, e -> {

            seconds[0]--;

            int min = seconds[0] / 60;
            int sec = seconds[0] % 60;

            timer.setText(String.format("%02d:%02d", min, sec));

            if (seconds[0] <= 0) {

                timerObject[0].stop();

                status.setText("🎉 ¡Sesión completada!");

                addXP(50);

                data.focusMinutes += 25;

                DataManager.save(data);

                JOptionPane.showMessageDialog(
                        this,
                        "Excelente trabajo.\nHas completado 25 minutos de concentración.\n\n+50 XP"
                );
            }
        });

        start.addActionListener(e -> {

            if (!timerObject[0].isRunning()) {

                timerObject[0].start();

                status.setText("🧠 Concentrándote...");

                start.setText("⏸ Pausar");

            } else {

                timerObject[0].stop();

                status.setText("Sesión pausada");

                start.setText("▶ Continuar");
            }
        });

        reset.addActionListener(e -> {

            timerObject[0].stop();

            seconds[0] = 1500;

            timer.setText("25:00");
            status.setText("Preparado para comenzar");
            start.setText("▶ Comenzar");
        });

        content.add(page, BorderLayout.CENTER);

        refresh();
    }

    private void showTraining() {

        content.removeAll();

        JPanel page = basePage();

        page.add(pageTitle("Entrenamiento cognitivo"));
        page.add(muted("Entrena diferentes habilidades relacionadas con la atención."));
        page.add(Box.createVerticalStrut(25));

        JPanel cards = new JPanel(new GridLayout(2, 2, 15, 15));
        cards.setOpaque(false);

        cards.add(trainingCard(
                "🎯",
                "Atención sostenida",
                "Mantén la concentración durante una actividad.",
                "sostenida"
        ));

        cards.add(trainingCard(
                "👁",
                "Atención selectiva",
                "Encuentra el objetivo entre diferentes distractores.",
                "selectiva"
        ));

        cards.add(trainingCard(
                "🛑",
                "Control inhibitorio",
                "Aprende a responder y detener respuestas impulsivas.",
                "inhibitorio"
        ));

        cards.add(trainingCard(
                "🧠",
                "Memoria de trabajo",
                "Entrena la capacidad de mantener información activa.",
                "memoria"
        ));

        page.add(cards);

        content.add(new JScrollPane(page), BorderLayout.CENTER);

        refresh();
    }

    private JPanel trainingCard(
            String icon,
            String name,
            String description,
            String type
    ) {

        JPanel card = cardPanel();

        JLabel title = new JLabel(icon + "  " + name);
        title.setForeground(TEXT);
        title.setFont(new Font("SansSerif", Font.BOLD, 18));

        JLabel desc = new JLabel(
                "<html><div style='width:220px'>" +
                        description +
                        "</div></html>"
        );

        desc.setForeground(MUTED);

        JButton button = primaryButton("Entrenar");

        button.addActionListener(e -> startTraining(type));

        card.add(title, BorderLayout.NORTH);
        card.add(desc, BorderLayout.CENTER);
        card.add(button, BorderLayout.SOUTH);

        return card;
    }

    private void startTraining(String type) {

        if (type.equals("sostenida")) {
            sustainedTraining();
        } else if (type.equals("selectiva")) {
            selectiveTraining();
        } else if (type.equals("inhibitorio")) {
            inhibitoryTraining();
        } else {
            memoryTraining();
        }
    }

    private void sustainedTraining() {

        final JDialog dialog = new JDialog(this, "Atención sostenida", true);
        dialog.setSize(550, 400);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BG);

        JLabel title = new JLabel(
                "<html><center>Cuando aparezca 🔵<br>haz clic en el botón.</center></html>",
                SwingConstants.CENTER
        );

        title.setForeground(TEXT);
        title.setFont(new Font("SansSerif", Font.BOLD, 20));

        JLabel stimulus = new JLabel("●", SwingConstants.CENTER);
        stimulus.setForeground(Color.RED);
        stimulus.setFont(new Font("SansSerif", Font.BOLD, 80));

        JButton button = primaryButton("RESPONDER");

        Random random = new Random();

        final int[] score = {0};
        final int[] round = {0};

        Timer change = new Timer(900, e -> {

            round[0]++;

            if (random.nextBoolean()) {
                stimulus.setForeground(BLUE);
            } else {
                stimulus.setForeground(RED);
            }

            if (round[0] >= 15) {

                ((Timer) e.getSource()).stop();

                data.trainingSessions++;
                data.trainingScore = Math.min(100,
                        data.trainingScore + 3);

                addXP(40);

                DataManager.save(data);

                JOptionPane.showMessageDialog(
                        dialog,
                        "Entrenamiento terminado.\n\n" +
                                "Aciertos: " + score[0] +
                                "\nPuntuación: " + (score[0] * 10) +
                                "\n\n+40 XP"
                );

                dialog.dispose();
            }
        });

        button.addActionListener(e -> {

            if (stimulus.getForeground().equals(BLUE)) {
                score[0]++;
                button.setText("✓ ¡Correcto!");
            } else {
                button.setText("✕ Distractor");
            }

            Timer reset = new Timer(300, x -> button.setText("RESPONDER"));
            reset.setRepeats(false);
            reset.start();
        });

        panel.add(title, BorderLayout.NORTH);
        panel.add(stimulus, BorderLayout.CENTER);
        panel.add(button, BorderLayout.SOUTH);

        dialog.setContentPane(panel);

        change.start();

        dialog.setVisible(true);
    }

    private void selectiveTraining() {

        String target = JOptionPane.showInputDialog(
                this,
                "Escribe la letra que debes encontrar:",
                "A"
        );

        if (target == null || target.isEmpty()) {
            return;
        }

        String letters = "BCDEFGHJKLMNPQRSTUVXYZ";

        Random random = new Random();

        int correct = 0;

        for (int i = 0; i < 5; i++) {

            StringBuilder sequence = new StringBuilder();

            for (int j = 0; j < 20; j++) {

                if (random.nextInt(5) == 0) {
                    sequence.append(target);
                } else {
                    sequence.append(
                            letters.charAt(
                                    random.nextInt(letters.length())
                            )
                    );
                }
            }

            String answer = JOptionPane.showInputDialog(
                    this,
                    "Encuentra '" + target + "' y escribe cuántas veces aparece:\n\n"
                            + sequence
            );

            try {

                int expected = countOccurrences(
                        sequence.toString(),
                        target.charAt(0)
                );

                if (answer != null &&
                        Integer.parseInt(answer) == expected) {
                    correct++;
                }

            } catch (Exception ignored) {
            }
        }

        int score = correct * 20;

        data.trainingScore = Math.min(
                100,
                data.trainingScore + correct
        );

        data.trainingSessions++;

        addXP(correct * 10);

        DataManager.save(data);

        JOptionPane.showMessageDialog(
                this,
                "Entrenamiento terminado.\n\n" +
                        "Rondas correctas: " + correct + "/5\n" +
                        "Puntuación: " + score +
                        "\n\n+" + (correct * 10) + " XP"
        );
    }

    private int countOccurrences(String text, char target) {

        int count = 0;

        for (char c : text.toCharArray()) {
            if (c == target) {
                count++;
            }
        }

        return count;
    }

    private void inhibitoryTraining() {

        String[] options = {
                "🟢 GO",
                "🔴 NO GO"
        };

        int correct = 0;

        for (int i = 0; i < 10; i++) {

            int answer = JOptionPane.showOptionDialog(
                    this,
                    "¿Debes responder?",
                    "Control inhibitorio",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    options,
                    options[0]
            );

            boolean shouldGo = new Random().nextBoolean();

            if ((shouldGo && answer == 0) ||
                    (!shouldGo && answer == 1)) {
                correct++;
            }
        }

        data.trainingScore = Math.min(
                100,
                data.trainingScore + correct
        );

        data.trainingSessions++;

        addXP(correct * 8);

        DataManager.save(data);

        JOptionPane.showMessageDialog(
                this,
                "Resultado:\n\n" +
                        correct + "/10 respuestas correctas\n\n" +
                        "+" + (correct * 8) + " XP"
        );
    }

    private void memoryTraining() {

        Random random = new Random();

        StringBuilder sequence = new StringBuilder();

        for (int i = 0; i < 5; i++) {

            sequence.append(
                    random.nextInt(9) + 1
            );
        }

        JOptionPane.showMessageDialog(
                this,
                "Memoriza esta secuencia:\n\n" +
                        sequence +
                        "\n\nPulsa OK cuando estés listo."
        );

        String answer = JOptionPane.showInputDialog(
                this,
                "Escribe la secuencia que recuerdes:"
        );

        int correct = sequence.toString().equals(answer)
                ? 1
                : 0;

        data.trainingSessions++;

        if (correct == 1) {
            data.trainingScore =
                    Math.min(100, data.trainingScore + 5);
            addXP(50);
        }

        DataManager.save(data);

        JOptionPane.showMessageDialog(
                this,
                correct == 1
                        ? "🧠 ¡Excelente memoria!\n+50 XP"
                        : "La secuencia era:\n" + sequence
        );
    }

    private void showProgress() {

        content.removeAll();

        JPanel page = basePage();

        page.add(pageTitle("Mi progreso"));
        page.add(muted("Observa cómo estás avanzando."));
        page.add(Box.createVerticalStrut(25));

        JPanel stats = new JPanel(new GridLayout(2, 2, 15, 15));
        stats.setOpaque(false);

        stats.add(bigStat(
                "⭐ XP total",
                String.valueOf(data.xp)
        ));

        stats.add(bigStat(
                "🏆 Nivel",
                String.valueOf(data.level)
        ));

        stats.add(bigStat(
                "⏱ Minutos enfocados",
                String.valueOf(data.focusMinutes)
        ));

        stats.add(bigStat(
                "🧠 Entrenamientos",
                String.valueOf(data.trainingSessions)
        ));

        page.add(stats);
        page.add(Box.createVerticalStrut(25));

        JPanel skills = cardPanel();

        skills.add(title("Habilidades"), BorderLayout.NORTH);

        JPanel inside = new JPanel();
        inside.setOpaque(false);
        inside.setLayout(new BoxLayout(inside, BoxLayout.Y_AXIS));

        addProgress(inside, "Atención", data.trainingScore);
        addProgress(inside, "Constancia",
                Math.min(100, data.streak * 5));
        addProgress(inside, "Metas",
                calculateGoalProgress());

        skills.add(inside, BorderLayout.CENTER);

        page.add(skills);

        content.add(new JScrollPane(page), BorderLayout.CENTER);

        refresh();
    }

    private JPanel bigStat(String name, String value) {

        JPanel panel = cardPanel();

        JLabel label = new JLabel(name);
        label.setForeground(MUTED);

        JLabel number = new JLabel(value);
        number.setForeground(TEXT);
        number.setFont(new Font("SansSerif", Font.BOLD, 32));

        panel.add(label, BorderLayout.NORTH);
        panel.add(number, BorderLayout.CENTER);

        return panel;
    }

    private JPanel basePage() {

        JPanel page = new JPanel();
        page.setBackground(BG);
        page.setLayout(new BoxLayout(page, BoxLayout.Y_AXIS));
        page.setBorder(new EmptyBorder(5, 5, 20, 5));

        return page;
    }

    private JPanel cardPanel() {

        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(PANEL);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        return panel;
    }

    private JPanel emptyPanel(
            String icon,
            String title,
            String description
    ) {

        JPanel panel = cardPanel();

        JLabel label = new JLabel(
                "<html><center><font size='7'>" +
                        icon +
                        "</font><br><br>" +
                        "<font size='5'>" +
                        title +
                        "</font><br><br>" +
                        "<font color='#9BA6B9'>" +
                        description +
                        "</font></center></html>",
                SwingConstants.CENTER
        );

        label.setForeground(TEXT);

        panel.add(label, BorderLayout.CENTER);

        return panel;
    }

    private JLabel pageTitle(String text) {

        JLabel label = new JLabel(text);
        label.setForeground(TEXT);
        label.setFont(new Font("SansSerif", Font.BOLD, 30));

        return label;
    }

    private JLabel title(String text) {

        JLabel label = new JLabel(text);
        label.setForeground(TEXT);
        label.setFont(new Font("SansSerif", Font.BOLD, 17));

        return label;
    }

    private JLabel muted(String text) {

        JLabel label = new JLabel(text);
        label.setForeground(MUTED);
        label.setFont(new Font("SansSerif", Font.PLAIN, 14));

        return label;
    }

    private JButton primaryButton(String text) {

        JButton button = new JButton(text);

        button.setForeground(Color.WHITE);
        button.setBackground(BLUE);
        button.setFont(new Font("SansSerif", Font.BOLD, 13));
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(12, 20, 12, 20));

        return button;
    }

    private JButton secondaryButton(String text) {

        JButton button = new JButton(text);

        button.setForeground(TEXT);
        button.setBackground(PANEL2);
        button.setFont(new Font("SansSerif", Font.PLAIN, 12));
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(10, 15, 10, 15));

        return button;
    }

    private String getPriorityTask() {

        for (Task task : data.tasks) {

            if (!task.completed) {
                return task.title;
            }
        }

        if (!data.goals.isEmpty()) {
            return data.goals.get(0).title;
        }

        return "Define tu primera meta";
    }

    private int calculateGoalProgress() {

        if (data.goals.isEmpty()) {
            return 0;
        }

        int total = 0;

        for (Goal goal : data.goals) {
            total += goal.progress;
        }

        return total / data.goals.size();
    }

    private int calculateTaskProgress() {

        if (data.tasks.isEmpty()) {
            return 0;
        }

        int completed = 0;

        for (Task task : data.tasks) {

            if (task.completed) {
                completed++;
            }
        }

        return completed * 100 / data.tasks.size();
    }

    private String getMotivation() {

        if (data.tasks.isEmpty() && data.goals.isEmpty()) {
            return "🌱 Todo empieza con una pequeña acción.";
        }

        if (calculateTaskProgress() == 100) {
            return "🔥 ¡Has completado tus tareas de hoy!";
        }

        return "🚀 No necesitas hacerlo todo. Solo empieza con la siguiente acción.";
    }

    private void addXP(int amount) {

        data.xp += amount;

        int required = data.level * 100;

        while (data.xp >= required) {

            data.xp -= required;
            data.level++;

            required = data.level * 100;

            JOptionPane.showMessageDialog(
                    this,
                    "🏆 ¡SUBISTE DE NIVEL!\n\nAhora eres nivel "
                            + data.level
            );
        }

        DataManager.save(data);
    }

    private void refresh() {

        content.revalidate();
        content.repaint();
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            try {
                UIManager.setLookAndFeel(
                        UIManager.getSystemLookAndFeelClassName()
                );
            } catch (Exception ignored) {
            }

            Main app = new Main();
            app.setVisible(true);
        });
    }

    // =========================
    // MODELOS
    // =========================

    static class AppData implements Serializable {

        String userName = "Usuario";

        int level = 1;
        int xp = 0;
        int streak = 1;

        int focusMinutes = 0;

        int trainingSessions = 0;
        int trainingScore = 0;

        List<Goal> goals = new ArrayList<>();
        List<Task> tasks = new ArrayList<>();
    }

    static class Goal implements Serializable {

        String title;
        String description;

        int progress = 0;
        boolean completed = false;

        Goal(String title, String description) {
            this.title = title;
            this.description = description;
        }
    }

    static class Task implements Serializable {

        String title;
        int minutes;

        boolean completed = false;

        Task(String title, int minutes) {
            this.title = title;
            this.minutes = minutes;
        }
    }

    // =========================
    // GUARDADO
    // =========================

    static class DataManager {

        private static final String FILE =
                "focusmind_data.dat";

        static void save(AppData data) {

            try (
                    ObjectOutputStream out =
                            new ObjectOutputStream(
                                    new FileOutputStream(FILE)
                            )
            ) {

                out.writeObject(data);

            } catch (IOException e) {

                e.printStackTrace();
            }
        }

        static AppData load() {

            File file = new File(FILE);

            if (!file.exists()) {
                return new AppData();
            }

            try (
                    ObjectInputStream in =
                            new ObjectInputStream(
                                    new FileInputStream(FILE)
                            )
            ) {

                return (AppData) in.readObject();

            } catch (Exception e) {

                return new AppData();
            }
        }
    }
}
