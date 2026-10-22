import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainMenu extends JFrame {

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

    public MainMenu() {
        data = DataManager.load();

        setTitle("FocusMind - Entrenador Cognitivo");
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
        sidebar.setPreferredSize(new Dimension(230, 0));
        sidebar.setBackground(new Color(10, 14, 24));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(new EmptyBorder(25, 15, 20, 15));

        JLabel logo = new JLabel("FOCUSMIND");
        logo.setForeground(TEXT);
        logo.setFont(new Font("SansSerif", Font.BOLD, 23));
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

        JLabel version = new JLabel("FocusMind 1.0");
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

            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(PANEL2);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(10, 14, 24));
            }
        });

        return button;
    }

    private void showDashboard() {

        content.removeAll();

        JPanel page = basePage();

        JLabel greeting = pageTitle("Hola, " + data.userName + " 👋");

        page.add(greeting);

        page.add(muted(
                "Convierte tus objetivos en acciones y entrena tu capacidad de concentración."
        ));

        page.add(Box.createVerticalStrut(25));

        JPanel stats = new JPanel(new GridLayout(1, 4, 15, 0));
        stats.setOpaque(false);

        stats.add(bigStat("⭐ XP", String.valueOf(data.xp)));
        stats.add(bigStat("🏆 Nivel", String.valueOf(data.level)));
        stats.add(bigStat("🔥 Racha", data.streak + " días"));
        stats.add(bigStat("⏱ Enfoque", data.focusMinutes + " min"));

        page.add(stats);

        page.add(Box.createVerticalStrut(25));

        JPanel cards = new JPanel(new GridLayout(1, 2, 20, 0));
        cards.setOpaque(false);

        JPanel priority = cardPanel();

        priority.add(title("🎯 Tu prioridad"), BorderLayout.NORTH);

        JLabel priorityText = new JLabel(
                "<html><div style='width:350px'>" +
                        "<font size='5'>" +
                        getPriorityTask() +
                        "</font><br><br>" +
                        "<font color='#9BA6B9'>" +
                        "Trabaja solamente en la siguiente acción." +
                        "</font></div></html>"
        );

        priorityText.setForeground(TEXT);

        priority.add(priorityText, BorderLayout.CENTER);

        JButton start = primaryButton("▶ Comenzar concentración");

        start.addActionListener(e -> showFocusTimer());

        priority.add(start, BorderLayout.SOUTH);

        JPanel progress = cardPanel();

        progress.add(title("📊 Estado actual"), BorderLayout.NORTH);

        JPanel bars = new JPanel();

        bars.setOpaque(false);
        bars.setLayout(new BoxLayout(bars, BoxLayout.Y_AXIS));

        addProgress(
                bars,
                "Metas",
                calculateGoalProgress()
        );

        addProgress(
                bars,
                "Tareas",
                calculateTaskProgress()
        );

        addProgress(
                bars,
                "Entrenamiento",
                data.trainingScore
        );

        progress.add(bars, BorderLayout.CENTER);

        cards.add(priority);
        cards.add(progress);

        page.add(cards);

        page.add(Box.createVerticalStrut(20));

        JPanel motivation = cardPanel();

        JLabel motivationText = new JLabel(
                "<html><font size='5'>" +
                        getMotivation() +
                        "</font></html>"
        );

        motivationText.setForeground(TEXT);

        motivation.add(motivationText, BorderLayout.CENTER);

        page.add(motivation);

        content.add(
                new JScrollPane(
                        page,
                        JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                        JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
                ),
                BorderLayout.CENTER
        );

        refresh();
    }

    private void showGoals() {

        content.removeAll();

        JPanel page = basePage();

        page.add(pageTitle("🎯 Mis metas"));

        page.add(muted(
                "Define lo que quieres conseguir y avanza poco a poco."
        ));

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
                    "Crea una meta para empezar."
            ));

        } else {

            for (Goal goal : data.goals) {

                JPanel card = cardPanel();

                JPanel left = new JPanel();

                left.setOpaque(false);
                left.setLayout(
                        new BoxLayout(left, BoxLayout.Y_AXIS)
                );

                JLabel goalTitle = title(goal.title);

                left.add(goalTitle);

                left.add(Box.createVerticalStrut(5));

                JLabel description = muted(
                        goal.description.isEmpty()
                                ? "Sin descripción"
                                : goal.description
                );

                left.add(description);

                left.add(Box.createVerticalStrut(15));

                JProgressBar bar =
                        new JProgressBar(0, 100);

                bar.setValue(goal.progress);
                bar.setStringPainted(true);
                bar.setForeground(GREEN);
                bar.setBackground(new Color(45, 53, 70));

                left.add(bar);

                JButton increase =
                        secondaryButton("+10%");

                increase.addActionListener(e -> {

                    int oldProgress = goal.progress;

                    goal.progress =
                            Math.min(
                                    100,
                                    goal.progress + 10
                            );

                    if (goal.progress == 100 &&
                            oldProgress < 100) {

                        goal.completed = true;

                        addXP(100);

                        JOptionPane.showMessageDialog(
                                this,
                                "🎉 ¡Meta completada!\n\n+100 XP"
                        );
                    }

                    DataManager.save(data);

                    showGoals();
                });

                JButton delete =
                        secondaryButton("Eliminar");

                delete.addActionListener(e -> {

                    int result =
                            JOptionPane.showConfirmDialog(
                                    this,
                                    "¿Quieres eliminar esta meta?",
                                    "Eliminar meta",
                                    JOptionPane.YES_NO_OPTION
                            );

                    if (result ==
                            JOptionPane.YES_OPTION) {

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

                page.add(
                        Box.createVerticalStrut(12)
                );
            }
        }

        content.add(
                new JScrollPane(page),
                BorderLayout.CENTER
        );

        refresh();
    }

    private void createGoal() {

        JTextField titleField =
                new JTextField();

        JTextArea descriptionField =
                new JTextArea(4, 25);

        JPanel fields = new JPanel();

        fields.setLayout(
                new BoxLayout(
                        fields,
                        BoxLayout.Y_AXIS
                )
        );

        fields.add(
                new JLabel("Nombre de la meta:")
        );

        fields.add(titleField);

        fields.add(
                Box.createVerticalStrut(10)
        );

        fields.add(
                new JLabel("Descripción:")
        );

        fields.add(descriptionField);

        int result =
                JOptionPane.showConfirmDialog(
                        this,
                        fields,
                        "Nueva meta",
                        JOptionPane.OK_CANCEL_OPTION
                );

        if (result ==
                JOptionPane.OK_OPTION) {

            String name =
                    titleField
                            .getText()
                            .trim();

            if (name.isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Escribe un nombre para la meta."
                );

                return;
            }

            data.goals.add(
                    new Goal(
                            name,
                            descriptionField
                                    .getText()
                                    .trim()
                    )
            );

            DataManager.save(data);

            showGoals();
        }
    }

    private void showTasks() {

        content.removeAll();

        JPanel page = basePage();

        page.add(pageTitle("✓ Tareas de hoy"));

        page.add(
                muted(
                        "Divide tus objetivos en acciones pequeñas."
                )
        );

        page.add(Box.createVerticalStrut(20));

        JButton add =
                primaryButton("+ Nueva tarea");

        add.addActionListener(
                e -> createTask()
        );

        JPanel top =
                new JPanel(new BorderLayout());

        top.setOpaque(false);

        top.add(
                add,
                BorderLayout.EAST
        );

        page.add(top);

        page.add(Box.createVerticalStrut(20));

        if (data.tasks.isEmpty()) {

            page.add(
                    emptyPanel(
                            "✓",
                            "No tienes tareas",
                            "Añade tu primera tarea del día."
                    )
            );

        } else {

            for (Task task : data.tasks) {

                JPanel card = cardPanel();

                JCheckBox check =
                        new JCheckBox(
                                task.title
                        );

                check.setSelected(
                        task.completed
                );

                check.setForeground(
                        task.completed
                                ? MUTED
                                : TEXT
                );

                check.setBackground(PANEL);

                check.setFont(
                        new Font(
                                "SansSerif",
                                Font.BOLD,
                                16
                        )
                );

                check.addActionListener(e -> {

                    if (check.isSelected()
                            && !task.completed) {

                        task.completed = true;

                        addXP(25);
                    }

                    DataManager.save(data);

                    showTasks();
                });

                JLabel duration =
                        muted(
                                task.minutes + " minutos"
                        );

                card.add(
                        check,
                        BorderLayout.CENTER
                );

                card.add(
                        duration,
                        BorderLayout.EAST
                );

                page.add(card);

                page.add(
                        Box.createVerticalStrut(10)
                );
            }
        }

        content.add(
                new JScrollPane(page),
                BorderLayout.CENTER
        );

        refresh();
    }

    private void createTask() {

        JTextField titleField =
                new JTextField();

        JSpinner minutes =
                new JSpinner(
                        new SpinnerNumberModel(
                                25,
                                1,
                                180,
                                1
                        )
                );

        JPanel fields =
                new JPanel();

        fields.setLayout(
                new BoxLayout(
                        fields,
                        BoxLayout.Y_AXIS
                )
        );

        fields.add(
                new JLabel("Tarea:")
        );

        fields.add(titleField);

        fields.add(
                Box.createVerticalStrut(10)
        );

        fields.add(
                new JLabel(
                        "Duración estimada:"
                )
        );

        fields.add(minutes);

        int result =
                JOptionPane.showConfirmDialog(
                        this,
                        fields,
                        "Nueva tarea",
                        JOptionPane.OK_CANCEL_OPTION
                );

        if (result ==
                JOptionPane.OK_OPTION) {

            String title =
                    titleField
                            .getText()
                            .trim();

            if (title.isEmpty()) {
                return;
            }

            data.tasks.add(
                    new Task(
                            title,
                            (Integer) minutes
                                    .getValue()
                    )
            );

            DataManager.save(data);

            showTasks();
        }
    }

    private void showTraining() {

        content.removeAll();

        JPanel page = basePage();

        page.add(
                pageTitle(
                        "🧠 Entrenamiento cognitivo"
                )
        );

        page.add(
                muted(
                        "Practica diferentes habilidades de atención y memoria."
                )
        );

        page.add(
                Box.createVerticalStrut(25)
        );

        JPanel cards =
                new JPanel(
                        new GridLayout(
                                2,
                                2,
                                15,
                                15
                        )
                );

        cards.setOpaque(false);

        cards.add(
                trainingCard(
                        "🎯",
                        "Atención sostenida",
                        "Mantén tu atención durante una actividad.",
                        "sostenida"
                )
        );

        cards.add(
                trainingCard(
                        "👁",
                        "Atención selectiva",
                        "Encuentra información importante entre distractores.",
                        "selectiva"
                )
        );

        cards.add(
                trainingCard(
                        "🛑",
                        "Control inhibitorio",
                        "Practica cuándo responder y cuándo detenerte.",
                        "inhibitorio"
                )
        );

        cards.add(
                trainingCard(
                        "🧠",
                        "Memoria de trabajo",
                        "Mantén información activa y utilízala.",
                        "memoria"
                )
        );

        page.add(cards);

        content.add(
                new JScrollPane(page),
                BorderLayout.CENTER
        );

        refresh();
    }

    private JPanel trainingCard(
            String icon,
            String name,
            String description,
            String type
    ) {

        JPanel card = cardPanel();

        card.add(
                title(icon + "  " + name),
                BorderLayout.NORTH
        );

        card.add(
                muted(
                        "<html><div style='width:230px'>"
                                + description
                                + "</div></html>"
                ),
                BorderLayout.CENTER
        );

        JButton button =
                primaryButton("Entrenar");

        button.addActionListener(
                e -> startTraining(type)
        );

        card.add(
                button,
                BorderLayout.SOUTH
        );

        return card;
    }

    private void startTraining(
            String type
    ) {

        switch (type) {

            case "sostenida":
                sustainedTraining();
                break;

            case "selectiva":
                selectiveTraining();
                break;

            case "inhibitorio":
                inhibitoryTraining();
                break;

            case "memoria":
                memoryTraining();
                break;
        }
    }

    private void sustainedTraining() {

        final JDialog dialog =
                new JDialog(
                        this,
                        "Atención sostenida",
                        true
                );

        dialog.setSize(550, 400);

        dialog.setLocationRelativeTo(this);

        JPanel panel =
                new JPanel(
                        new BorderLayout()
                );

        panel.setBackground(BG);

        JLabel instruction =
                new JLabel(
                        "<html><center>"
                                + "Cuando aparezca el círculo AZUL,<br>"
                                + "presiona RESPONDER."
                                + "</center></html>",
                        SwingConstants.CENTER
                );

        instruction.setForeground(TEXT);

        instruction.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        19
                )
        );

        JLabel stimulus =
                new JLabel(
                        "●",
                        SwingConstants.CENTER
                );

        stimulus.setForeground(RED);

        stimulus.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        80
                )
        );

        JButton button =
                primaryButton(
                        "RESPONDER"
                );

        Random random =
                new Random();

        final int[] score = {0};

        final int[] round = {0};

        final boolean[] blue = {false};

        Timer change =
                new Timer(
                        900,
                        e -> {

                            round[0]++;

                            blue[0] =
                                    random.nextBoolean();

                            stimulus.setForeground(
                                    blue[0]
                                            ? BLUE
                                            : RED
                            );

                            if (round[0] >= 20) {

                                ((Timer)e.getSource())
                                        .stop();

                                data.trainingSessions++;

                                data.trainingScore =
                                        Math.min(
                                                100,
                                                data.trainingScore
                                                        + score[0]
                        );

                                addXP(
                                        score[0] * 5
                                );

                                DataManager.save(
                                        data
                                );

                                JOptionPane.showMessageDialog(
                                        dialog,
                                        "Entrenamiento terminado.\n\n"
                                                + "Aciertos: "
                                                + score[0]
                                                + "\nRondas: 20"
                                                + "\n\n+"
                                                + (score[0] * 5)
                                                + " XP"
                                );

                                dialog.dispose();
                            }
                        }
                );

        button.addActionListener(e -> {

            if (blue[0]) {

                score[0]++;

                button.setText(
                        "✓ Correcto"
                );

            } else {

                button.setText(
                        "✕ Distractor"
                );
            }

            Timer reset =
                    new Timer(
                            300,
                            x ->
                                    button.setText(
                                            "RESPONDER"
                                    )
                    );

            reset.setRepeats(false);

            reset.start();
        });

        panel.add(
                instruction,
                BorderLayout.NORTH
        );

        panel.add(
                stimulus,
                BorderLayout.CENTER
        );

        panel.add(
                button,
                BorderLayout.SOUTH
        );

        dialog.setContentPane(panel);

        change.start();

        dialog.setVisible(true);
    }

    private void selectiveTraining() {

        String target =
                JOptionPane.showInputDialog(
                        this,
                        "Escribe una letra objetivo:",
                        "A"
                );

        if (target == null
                || target.isEmpty()) {
            return;
        }

        char targetChar =
                target.charAt(0);

        String letters =
                "BCDEFGHJKLMNPQRSTUVXYZ";

        Random random =
                new Random();

        int correct = 0;

        for (int round = 0;
                round < 5;
                round++) {

            StringBuilder sequence =
                    new StringBuilder();

            for (int i = 0;
                    i < 25;
                    i++) {

                if (random.nextInt(5) == 0) {

                    sequence.append(
                            targetChar
                    );

                } else {

                    sequence.append(
                            letters.charAt(
                                    random.nextInt(
                                            letters.length()
                                    )
                            )
                    );
                }
            }

            String answer =
                    JOptionPane.showInputDialog(
                            this,
                            "¿Cuántas veces aparece '"
                                    + targetChar
                                    + "'?\n\n"
                                    + sequence
                    );

            try {

                int expected =
                        countOccurrences(
                                sequence.toString(),
                                targetChar
                        );

                if (answer != null
                        && Integer.parseInt(answer)
                        == expected) {

                    correct++;
                }

            } catch (Exception ignored) {
            }
        }

        data.trainingSessions++;

        data.trainingScore =
                Math.min(
                        100,
                        data.trainingScore
                                + correct
                );

        addXP(
                correct * 10
        );

        DataManager.save(data);

        JOptionPane.showMessageDialog(
                this,
                "Resultado\n\n"
                        + "Rondas correctas: "
                        + correct
                        + "/5\n\n+"
                        + (correct * 10)
                        + " XP"
        );
    }

    private int countOccurrences(
            String text,
            char target
    ) {

        int count = 0;

        for (char character :
                text.toCharArray()) {

            if (character == target) {
                count++;
            }
        }

        return count;
    }

    private void inhibitoryTraining() {

        Random random =
                new Random();

        int correct = 0;

        for (int i = 0;
                i < 10;
                i++) {

            boolean shouldRespond =
                    random.nextBoolean();

            String message =
                    shouldRespond
                            ? "🟢 GO\n\nRESPONDE"
                            : "🔴 NO GO\n\nNO RESPONDAS";

            int option =
                    JOptionPane.showConfirmDialog(
                            this,
                            message,
                            "Control inhibitorio",
                            JOptionPane.YES_NO_OPTION
                    );

            boolean userResponded =
                    option == JOptionPane.YES_OPTION;

            if (userResponded
                    == shouldRespond) {

                correct++;
            }
        }

        data.trainingSessions++;

        data.trainingScore =
                Math.min(
                        100,
                        data.trainingScore
                                + correct
                );

        addXP(
                correct * 8
        );

        DataManager.save(data);

        JOptionPane.showMessageDialog(
                this,
                "Resultado\n\n"
                        + correct
                        + "/10 correctas\n\n+"
                        + (correct * 8)
                        + " XP"
        );
    }

    private void memoryTraining() {

        Random random =
                new Random();

        StringBuilder sequence =
                new StringBuilder();

        for (int i = 0;
                i < 5;
                i++) {

            sequence.append(
                    random.nextInt(9) + 1
            );
        }

        JOptionPane.showMessageDialog(
                this,
                "Memoriza la secuencia:\n\n"
                        + sequence
                        + "\n\nPulsa OK cuando estés listo."
        );

        String answer =
                JOptionPane.showInputDialog(
                        this,
                        "Escribe la secuencia:"
                );

        boolean correct =
                sequence.toString()
                        .equals(answer);

        data.trainingSessions++;

        if (correct) {

            data.trainingScore =
                    Math.min(
                            100,
                            data.trainingScore + 5
                    );

            addXP(50);
        }

        DataManager.save(data);

        JOptionPane.showMessageDialog(
                this,
                correct
                        ? "🧠 ¡Excelente!\n\n+50 XP"
                        : "La secuencia era:\n"
                        + sequence
        );
    }

    private void showFocusTimer() {

        content.removeAll();

        JPanel page = basePage();

        page.add(
                pageTitle(
                        "◷ Modo concentración"
                )
        );

        page.add(
                muted(
                        "Una tarea. Un objetivo. Un momento."
                )
        );

        page.add(
                Box.createVerticalStrut(50)
        );

        JPanel center =
                new JPanel();

        center.setOpaque(false);

        center.setLayout(
                new BoxLayout(
                        center,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel timer =
                new JLabel(
                        "25:00",
                        SwingConstants.CENTER
                );

        timer.setForeground(TEXT);

        timer.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        80
                )
        );

        timer.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        JLabel status =
                muted(
                        "Preparado para comenzar"
                );

        status.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        JButton start =
                primaryButton(
                        "▶ Comenzar"
                );

        start.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        JButton reset =
                secondaryButton(
                        "Reiniciar"
                );

        reset.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        center.add(timer);

        center.add(
                Box.createVerticalStrut(10)
        );

        center.add(status);

        center.add(
                Box.createVerticalStrut(30)
        );

        center.add(start);

        center.add(
                Box.createVerticalStrut(10)
        );

        center.add(reset);

        page.add(center);

        final int[] seconds =
                {1500};

        final Timer[] timerObject =
                new Timer[1];

        timerObject[0] =
                new Timer(
                        1000,
                        e -> {

                            seconds[0]--;

                            int minutes =
                                    seconds[0] / 60;

                            int secondsRemaining =
                                    seconds[0] % 60;

                            timer.setText(
                                    String.format(
                                            "%02d:%02d",
                                            minutes,
                                            secondsRemaining
                                    )
                            );

                            if (seconds[0] <= 0) {

                                timerObject[0]
                                        .stop();

                                status.setText(
                                        "🎉 ¡Sesión completada!"
                                );

                                data.focusMinutes +=
                                        25;

                                addXP(50);

                                DataManager.save(
                                        data
                                );

                                JOptionPane.showMessageDialog(
                                        this,
                                        "¡Excelente trabajo!\n\n"
                                                + "25 minutos completados.\n\n"
                                                + "+50 XP"
                                );

                                start.setText(
                                        "▶ Comenzar"
                                );
                            }
                        }
                );

        start.addActionListener(e -> {

            if (timerObject[0]
                    .isRunning()) {

                timerObject[0].stop();

                start.setText(
                        "▶ Continuar"
                );

                status.setText(
                        "Sesión pausada"
                );

            } else {

                timerObject[0].start();

                start.setText(
                        "⏸ Pausar"
                );

                status.setText(
                        "🧠 Concentrándote..."
                );
            }
        });

        reset.addActionListener(e -> {

            timerObject[0].stop();

            seconds[0] = 1500;

            timer.setText("25:00");

            start.setText(
                    "▶ Comenzar"
            );

            status.setText(
                    "Preparado para comenzar"
            );
        });

        content.add(
                page,
                BorderLayout.CENTER
        );

        refresh();
    }

    private void showProgress() {

        content.removeAll();

        JPanel page = basePage();

        page.add(
                pageTitle("▥ Mi progreso")
        );

        page.add(
                muted(
                        "Observa cómo estás evolucionando."
                )
        );

        page.add(
                Box.createVerticalStrut(25)
        );

        JPanel stats =
                new JPanel(
                        new GridLayout(
                                2,
                                2,
                                15,
                                15
                        )
                );

        stats.setOpaque(false);

        stats.add(
                bigStat(
                        "⭐ XP",
                        String.valueOf(data.xp)
                )
        );

        stats.add(
                bigStat(
                        "🏆 Nivel",
                        String.valueOf(data.level)
                )
        );

        stats.add(
                bigStat(
                        "⏱ Minutos",
                        String.valueOf(
                                data.focusMinutes
                        )
                )
        );

        stats.add(
                bigStat(
                        "🧠 Sesiones",
                        String.valueOf(
                                data.trainingSessions
                        )
        ));

        page.add(stats);

        page.add(
                Box.createVerticalStrut(25)
        );

        JPanel skills = cardPanel();

        skills.add(
                title("Habilidades"),
                BorderLayout.NORTH
        );

        JPanel bars = new JPanel();

        bars.setOpaque(false);

        bars.setLayout(
                new BoxLayout(
                        bars,
                        BoxLayout.Y_AXIS
                )
        );

        addProgress(
                bars,
                "Atención",
                data.trainingScore
        );

        addProgress(
                bars,
                "Constancia",
                Math.min(
                        100,
                        data.streak * 5
                )
        );

        addProgress(
                bars,
                "Metas",
                calculateGoalProgress()
        );

        addProgress(
                bars,
                "Tareas",
                calculateTaskProgress()
        );

        skills.add(
                bars,
                BorderLayout.CENTER
        );

        page.add(skills);

        content.add(
                new JScrollPane(page),
                BorderLayout.CENTER
        );

        refresh();
    }

    private JPanel bigStat(
            String name,
            String value
    ) {

        JPanel panel =
                cardPanel();

        panel.add(
                muted(name),
                BorderLayout.NORTH
        );

        JLabel number =
                new JLabel(value);

        number.setForeground(TEXT);

        number.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        30
                )
        );

        panel.add(
                number,
                BorderLayout.CENTER
        );

        return panel;
    }

    private JPanel cardPanel() {

        JPanel panel =
                new JPanel(
                        new BorderLayout(
                                15,
                                15
                        )
                );

        panel.setBackground(PANEL);

        panel.setBorder(
                new EmptyBorder(
                        20,
                        20,
                        20,
                        20
                )
        );

        return panel;
    }

    private JPanel basePage() {

        JPanel page =
                new JPanel();

        page.setBackground(BG);

        page.setBorder(
                new EmptyBorder(
                        5,
                        5,
                        20,
                        5
                )
        );

        page.setLayout(
                new BoxLayout(
                        page,
                        BoxLayout.Y_AXIS
                )
        );

        return page;
    }

    private JPanel emptyPanel(
            String icon,
            String title,
            String description
    ) {

        JPanel panel =
                cardPanel();

        JLabel text =
                new JLabel(
                        "<html><center>"
                                + "<font size='7'>"
                                + icon
                                + "</font><br><br>"
                                + "<font size='5'>"
                                + title
                                + "</font><br><br>"
                                + "<font color='#9BA6B9'>"
                                + description
                                + "</font>"
                                + "</center></html>",
                        SwingConstants.CENTER
                );

        text.setForeground(TEXT);

        panel.add(
                text,
                BorderLayout.CENTER
        );

        return panel;
    }

    private JLabel pageTitle(
            String text
    ) {

        JLabel label =
                new JLabel(text);

        label.setForeground(TEXT);

        label.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        30
                )
        );

        return label;
    }

    private JLabel title(
            String text
    ) {

        JLabel label =
                new JLabel(text);

        label.setForeground(TEXT);

        label.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        17
                )
        );

        return label;
    }

    private JLabel muted(
            String text
    ) {

        JLabel label =
                new JLabel(text);

        label.setForeground(MUTED);

        label.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        14
                )
        );

        return label;
    }

    private JButton primaryButton(
            String text
    ) {

        JButton button =
                new JButton(text);

        button.setForeground(Color.WHITE);

        button.setBackground(BLUE);

        button.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        13
                )
        );

        button.setFocusPainted(false);

        button.setBorder(
                new EmptyBorder(
                        12,
                        20,
                        12,
                        20
                )
        );

        return button;
    }

    private JButton secondaryButton(
            String text
    ) {

        JButton button =
                new JButton(text);

        button.setForeground(TEXT);

        button.setBackground(PANEL2);

        button.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        12
                )
        );

        button.setFocusPainted(false);

        button.setBorder(
                new EmptyBorder(
                        10,
                        15,
                        10,
                        15
                )
        );

        return button;
    }

    private void addProgress(
            JPanel panel,
            String name,
            int percentage
    ) {

        percentage =
                Math.max(
                        0,
                        Math.min(
                                100,
                                percentage
                        )
                );

        JLabel label =
                new JLabel(
                        name
                                + "    "
                                + percentage
                                + "%"
                );

        label.setForeground(TEXT);

        JProgressBar bar =
                new JProgressBar(
                        0,
                        100
                );

        bar.setValue(percentage);

        bar.setForeground(BLUE);

        bar.setBackground(
                new Color(
                        45,
                        53,
                        70
                )
        );

        bar.setBorderPainted(false);

        panel.add(label);

        panel.add(bar);

        panel.add(
                Box.createVerticalStrut(15)
        );
    }

    private String getPriorityTask() {

        for (Task task :
                data.tasks) {

            if (!task.completed) {
                return task.title;
            }
        }

        if (!data.goals.isEmpty()) {
            return data.goals
                    .get(0)
                    .title;
        }

        return "Crea tu primera meta";
    }

    private int calculateGoalProgress() {

        if (data.goals.isEmpty()) {
            return 0;
        }

        int total = 0;

        for (Goal goal :
                data.goals) {

            total += goal.progress;
        }

        return total /
                data.goals.size();
    }

    private int calculateTaskProgress() {

        if (data.tasks.isEmpty()) {
            return 0;
        }

        int completed = 0;

        for (Task task :
                data.tasks) {

            if (task.completed) {
                completed++;
            }
        }

        return completed * 100 /
                data.tasks.size();
    }

    private String getMotivation() {

        if (data.tasks.isEmpty()
                && data.goals.isEmpty()) {

            return "🌱 Todo comienza con una pequeña acción.";
        }

        if (calculateTaskProgress()
                == 100) {

            return "🔥 ¡Has completado todas tus tareas!";
        }

        return "🚀 No necesitas hacerlo todo ahora. Concéntrate en la siguiente acción.";
    }

    private void addXP(
            int amount
    ) {

        data.xp += amount;

        int required =
                data.level * 100;

        while (data.xp >= required) {

            data.xp -= required;

            data.level++;

            required =
                    data.level * 100;

            JOptionPane.showMessageDialog(
                    this,
                    "🏆 ¡SUBISTE DE NIVEL!\n\n"
                            + "Ahora eres nivel "
                            + data.level
            );
        }

        DataManager.save(data);
    }

    private void refresh() {

        content.revalidate();

        content.repaint();
    }

    public static void main(
            String[] args
    ) {

        SwingUtilities.invokeLater(
                () -> {

                    try {

                        UIManager.setLookAndFeel(
                                UIManager
                                        .getSystemLookAndFeelClassName()
                        );

                    } catch (Exception ignored) {
                    }

                    MainMenu app =
                            new MainMenu();

                    app.setVisible(true);
                }
        );
    }

    // =====================================================
    // DATOS
    // =====================================================

    static class AppData
            implements Serializable {

        String userName =
                "Usuario";

        int level = 1;

        int xp = 0;

        int streak = 1;

        int focusMinutes = 0;

        int trainingSessions = 0;

        int trainingScore = 0;

        List<Goal> goals =
                new ArrayList<>();

        List<Task> tasks =
                new ArrayList<>();
    }

    static class Goal
            implements Serializable {

        String title;

        String description;

        int progress = 0;

        boolean completed = false;

        Goal(
                String title,
                String description
        ) {

            this.title = title;

            this.description =
                    description;
        }
    }

    static class Task
            implements Serializable {

        String title;

        int minutes;

        boolean completed = false;

        Task(
                String title,
                int minutes
        ) {

            this.title = title;

            this.minutes = minutes;
        }
    }

    // =====================================================
    // GUARDADO
    // =====================================================

    static class DataManager {

        private static final String FILE =
                "focusmind_data.dat";

        static void save(
                AppData data
        ) {

            try (
                    ObjectOutputStream out =
                            new ObjectOutputStream(
                                    new FileOutputStream(
                                            FILE
                                    )
                            )
            ) {

                out.writeObject(data);

            } catch (IOException e) {

                e.printStackTrace();
            }
        }

        static AppData load() {

            File file =
                    new File(FILE);

            if (!file.exists()) {

                return new AppData();
            }

            try (
                    ObjectInputStream in =
                            new ObjectInputStream(
                                    new FileInputStream(
                                            FILE
                                    )
                            )
            ) {

                return (AppData)
                        in.readObject();

            } catch (Exception e) {

                return new AppData();
            }
        }
    }
}

       