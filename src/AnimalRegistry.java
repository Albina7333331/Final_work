import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

abstract class Animal {
    private String name;
    private String birthDate;
    private List<String> commands;

    public Animal(String name, String birthDate) {
        this.name = name;
        this.birthDate = birthDate;
        this.commands = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public List<String> getCommands() {
        return commands;
    }

    public void addCommand(String command) {
        commands.add(command);
    }

    @Override
    public String toString() {
        return "Name: " + name + ", BirthDate: " + birthDate + ", Commands: " + commands;
    }
}

abstract class Pet extends Animal {
    public Pet(String name, String birthDate) {
        super(name, birthDate);
    }
}

abstract class PackAnimal extends Animal {
    public PackAnimal(String name, String birthDate) {
        super(name, birthDate);
    }
}

class Dog extends Pet {
    public Dog(String name, String birthDate) {
        super(name, birthDate);
    }
}

class Cat extends Pet {
    public Cat(String name, String birthDate) {
        super(name, birthDate);
    }
}

class Hamster extends Pet {
    public Hamster(String name, String birthDate) {
        super(name, birthDate);
    }
}

class Horse extends PackAnimal {
    public Horse(String name, String birthDate) {
        super(name, birthDate);
    }
}

class Camel extends PackAnimal {
    public Camel(String name, String birthDate) {
        super(name, birthDate);
    }
}

class Donkey extends PackAnimal {
    public Donkey(String name, String birthDate) {
        super(name, birthDate);
    }
}

class Counter implements AutoCloseable {
    private int count = 0;
    private boolean closed = false;
    private boolean usedInTryWithResources = false;

    public Counter() {
        this.usedInTryWithResources = true;
    }

    public void add() {
        if (closed) {
            throw new IllegalStateException("Counter is already closed.");
        }
        count++;
    }

    public int getCount() {
        return count;
    }

    @Override
    public void close() {
        closed = true;
        if (!usedInTryWithResources) {
            throw new IllegalStateException("Counter was not used in try-with-resources.");
        }
    }

    public boolean isClosed() {
        return closed;
    }
}

public class AnimalRegistry {
    private static Map<String, Animal> animals = new HashMap<>();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            printMenu();
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline
            switch (choice) {
                case 1:
                    try (Counter counter = new Counter()) {
                        registerAnimal(counter);
                    } catch (Exception e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;
                case 2:
                    listAnimalCommands();
                    break;
                case 3:
                    trainAnimal();
                    break;
                case 4:
                    listAllAnimals();
                    break;
                case 5:
                    countPets();
                    break;
                case 6:
                    countPackAnimals();
                    break;
                case 7:
                    System.out.println("Выход из программы.");
                    System.exit(0);
                default:
                    System.out.println("Неверный выбор. Пожалуйста, выберите снова.");
            }
        }
    }

    private static void printMenu() {
        System.out.println("\n--- Меню ---");
        System.out.println("1. Завести новое животное");
        System.out.println("2. Показать команды животного");
        System.out.println("3. Обучить животное новой команде");
        System.out.println("4. Показать всех животных");
        System.out.println("5. Показать количество домашних животных");
        System.out.println("6. Показать количество вьючных животных");
        System.out.println("7. Выход");
        System.out.print("Выберите опцию: ");
    }

    private static void registerAnimal(Counter counter) throws Exception {
        System.out.print("Введите имя животного: ");
        String name = scanner.nextLine();
        System.out.print("Введите дату рождения (гггг-мм-дд): ");
        String birthDate = scanner.nextLine();
        System.out.print("Введите тип животного (Dog, Cat, Hamster, Horse, Camel, Donkey): ");
        String type = scanner.nextLine();

        if (name.isEmpty() || birthDate.isEmpty() || type.isEmpty()) {
            System.out.println("Ошибка: Все поля должны быть заполнены.");
            return;
        }

        Animal animal;
        switch (type.toLowerCase()) {
            case "dog":
                animal = new Dog(name, birthDate);
                break;
            case "cat":
                animal = new Cat(name, birthDate);
                break;
            case "hamster":
                animal = new Hamster(name, birthDate);
                break;
            case "horse":
                animal = new Horse(name, birthDate);
                break;
            case "camel":
                animal = new Camel(name, birthDate);
                break;
            case "donkey":
                animal = new Donkey(name, birthDate);
                break;
            default:
                System.out.println("Неизвестный тип животного.");
                return;
        }

        animals.put(name, animal);
        counter.add();
        System.out.println("Животное успешно зарегистрировано. Текущее количество животных: " + counter.getCount());

        // Сохранение списка животных в файл после регистрации нового животного
        saveAnimalsToFile("animals.txt");
    }

    private static void listAnimalCommands() {
        System.out.print("Введите имя животного: ");
        String name = scanner.nextLine();
        Animal animal = animals.get(name);
        if (animal != null) {
            System.out.println("Команды животного " + name + ": " + animal.getCommands());
        } else {
            System.out.println("Животное с именем " + name + " не найдено.");
        }
    }

    private static void trainAnimal() {
        System.out.print("Введите имя животного: ");
        String name = scanner.nextLine();
        Animal animal = animals.get(name);
        if (animal != null) {
            System.out.print("Введите новую команду для животного " + name + ": ");
            String command = scanner.nextLine();
            animal.addCommand(command);
            System.out.println("Животное " + name + " успешно обучено команде: " + command);
        } else {
            System.out.println("Животное с именем " + name + " не найдено.");
        }
    }

    private static void listAllAnimals() {
        System.out.println("\nСписок всех животных:");
        for (Animal animal : animals.values()) {
            System.out.println(animal);
        }
    }

    private static void countPets() {
        long petCount = animals.values().stream()
                .filter(animal -> animal instanceof Pet)
                .count();
        System.out.println("Количество домашних животных: " + petCount);
    }

    private static void countPackAnimals() {
        long packAnimalCount = animals.values().stream()
                .filter(animal -> animal instanceof PackAnimal)
                .count();
        System.out.println("Количество вьючных животных: " + packAnimalCount);
    }

    private static void saveAnimalsToFile(String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (Animal animal : animals.values()) {
                writer.write(animal.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Ошибка при записи в файл: " + e.getMessage());
        }
    }
}
