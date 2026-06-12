import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// 联系人实体类
class Contact {
    private String name;
    private String phone;
    private String group;
    private String remark;

    public Contact(String name, String phone, String group, String remark) {
        this.name = name;
        this.phone = phone;
        this.group = group;
        this.remark = remark;
    }

    // Getter & Setter
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "姓名: " + name + ", 电话: " + phone + ", 分组: " + group + ", 备注: " + remark;
    }

    // 转为文件存储格式（逗号分隔）
    public String toFileLine() {
        return name + "," + phone + "," + group + "," + remark;
    }

    // 从一行文本解析为联系人对象
    public static Contact parse(String line) {
        String[] arr = line.split(",");
        return new Contact(arr[0], arr[1], arr[2], arr[3]);
    }
}

// 通讯录主程序（带文件持久化）
public class AddressBookSystem {
    private static final String FILE_PATH = "contact.txt";
    private static List<Contact> contactList = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        // 程序启动：加载本地文件数据
        loadDataFromFile();
        System.out.println("通讯录加载完成！");

        while (true) {
            printMenu();
            int choice = scanner.nextInt();
            scanner.nextLine(); // 吸收换行符

            switch (choice) {
                case 1:
                    addContact();
                    break;
                case 2:
                    queryContact();
                    break;
                case 3:
                    updateContact();
                    break;
                case 4:
                    deleteContact();
                    break;
                case 5:
                    showAllContacts();
                    break;
                case 0:
                    // 退出前保存数据
                    saveDataToFile();
                    System.out.println("数据已保存，退出系统，再见！");
                    return;
                default:
                    System.out.println("无效选项，请重新输入！");
            }
        }
    }

    // 打印主菜单
    private static void printMenu() {
        System.out.println("\n===== 简易通讯录管理系统 =====");
        System.out.println("1. 添加联系人");
        System.out.println("2. 查询联系人");
        System.out.println("3. 修改联系人");
        System.out.println("4. 删除联系人");
        System.out.println("5. 显示所有联系人");
        System.out.println("0. 退出系统");
        System.out.print("请输入你的选择：");
    }

    // 添加联系人
    private static void addContact() {
        System.out.print("请输入联系人姓名：");
        String name = scanner.nextLine();
        System.out.print("请输入联系人电话：");
        String phone = scanner.nextLine();
        System.out.print("请输入联系人分组（家人/朋友/同事）：");
        String group = scanner.nextLine();
        System.out.print("请输入备注：");
        String remark = scanner.nextLine();

        contactList.add(new Contact(name, phone, group, remark));
        System.out.println("联系人添加成功！");
    }

    // 模糊查询（姓名/电话）
    private static void queryContact() {
        System.out.print("请输入查询关键词（姓名/电话）：");
        String keyword = scanner.nextLine();
        boolean found = false;

        System.out.println("===== 查询结果 =====");
        for (Contact c : contactList) {
            if (c.getName().contains(keyword) || c.getPhone().contains(keyword)) {
                System.out.println(c);
                found = true;
            }
        }
        if (!found) {
            System.out.println("未找到匹配联系人！");
        }
    }

    // 修改联系人
    private static void updateContact() {
        System.out.print("请输入要修改的联系人姓名：");
        String name = scanner.nextLine();
        Contact target = null;

        for (Contact c : contactList) {
            if (c.getName().equals(name)) {
                target = c;
                break;
            }
        }

        if (target == null) {
            System.out.println("未找到该联系人！");
            return;
        }

        System.out.print("新姓名(原:" + target.getName() + ")：");
        String newName = scanner.nextLine();
        System.out.print("新电话(原:" + target.getPhone() + ")：");
        String newPhone = scanner.nextLine();
        System.out.print("新分组(原:" + target.getGroup() + ")：");
        String newGroup = scanner.nextLine();
        System.out.print("新备注(原:" + target.getRemark() + ")：");
        String newRemark = scanner.nextLine();

        target.setName(newName);
        target.setPhone(newPhone);
        target.setGroup(newGroup);
        target.setRemark(newRemark);
        System.out.println("信息修改成功！");
    }

    // 删除联系人
    private static void deleteContact() {
        System.out.print("请输入要删除的联系人姓名：");
        String name = scanner.nextLine();
        Contact target = null;

        for (Contact c : contactList) {
            if (c.getName().equals(name)) {
                target = c;
                break;
            }
        }

        if (target == null) {
            System.out.println("未找到该联系人！");
            return;
        }

        contactList.remove(target);
        System.out.println("联系人删除成功！");
    }

    // 展示全部联系人
    private static void showAllContacts() {
        if (contactList.isEmpty()) {
            System.out.println("通讯录暂无数据！");
            return;
        }
        System.out.println("===== 全部联系人 =====");
        for (Contact c : contactList) {
            System.out.println(c);
        }
    }

    // 数据写入本地文件
    private static void saveDataToFile() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Contact c : contactList) {
                bw.write(c.toFileLine());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("数据保存失败：" + e.getMessage());
        }
    }

    // 从本地文件读取数据
    private static void loadDataFromFile() {
        File file = new File(FILE_PATH);
        // 文件不存在则直接返回
        if (!file.exists()) {
            return;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    contactList.add(Contact.parse(line));
                }
            }
        } catch (IOException e) {
            System.out.println("数据读取失败：" + e.getMessage());
        }
    }
}