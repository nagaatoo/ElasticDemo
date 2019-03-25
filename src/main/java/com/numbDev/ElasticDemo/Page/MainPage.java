package com.numbDev.ElasticDemo.Page;

import com.numbDev.ElasticDemo.Base.DAO.UserDAO;
import com.numbDev.ElasticDemo.Base.User;
import com.numbDev.ElasticDemo.Service.UserService;
import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.grid.ColumnResizeMode;
import com.vaadin.shared.ui.grid.HeightMode;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;

import java.util.List;

@Theme("valo")
@SpringUI
public final class MainPage extends UI {

    private static final int DATE_FIELD_LENGHT = 194;
    private static final String UUID_COLUMN_NAME = "Id";
    private static final String NAME_COLUMN = "Тайтл";
    private static final String ADDRESS_COLUMN = "Контент";
    private static final String BODY_TYPE_COLUMN = "Тип тела";
    private static final String CHILDS_COLUMN = "Дети?";
    private static final String PROFESSION_COLUMN = "Профессия";
    private Grid<User> table;
    private Filter filter;
    private Manage manage;

    private final UserService service;
    private final UserDAO dao;

    public MainPage(UserService service, UserDAO dao) {
        this.service = service;
        this.dao = dao;
    }

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        VerticalLayout vlRoot = new VerticalLayout();
        VerticalLayout vlLog = new VerticalLayout();
        HorizontalLayout hzArea = new HorizontalLayout();
        vlLog.setMargin(false);
        vlLog.setSizeFull();
        table = new Grid<>();
        table.addColumn(User::getId).setCaption(UUID_COLUMN_NAME);
        table.addColumn(User::getTitle).setCaption(NAME_COLUMN);
       // table.addColumn(User::getContent).setCaption(ADDRESS_COLUMN);

        table.setHeightMode(HeightMode.ROW);
//        table.setColumnResizeMode(ColumnResizeMode.SIMPLE);
        table.setSizeFull();
       // vlLog.addComponents(table);

        hzArea.setWidth("100%");
        manage = new Manage();
        filter = new Filter();

        hzArea.addComponents(filter); //manage

        vlRoot.setSizeFull();
        vlRoot.setMargin(true);

        vlRoot.addComponents(hzArea, table);
        vlRoot.setExpandRatio(table, 0.8f);
        double test = service.count();
        table.setDataProvider(
            (sortOrders, offset, limit) -> service.getAll(offset, limit + offset).stream(), service::count);
            //getTableData(offset, limit + offset)
        this.setContent(vlRoot);
    }

    private List<User> getTableData(int begin, int end) {
        return service.getAll(0, 1000);
    }

    class Manage extends Panel {

        private TextField UUIDFilter = new TextField("UUID / Рассылка");
        private TextField requestUUIDFilter = new TextField("UUID ответа");
        private DateTimeField creationDateBeginFilter = new DateTimeField("Создан с ");
        private DateTimeField creationDateEndFilter = new DateTimeField("Создан по ");
        private DateTimeField sendTimeBeginFilter = new DateTimeField("Отправлен с");
        private DateTimeField sendTimeEndFilter = new DateTimeField("Отправлен по");
        private ComboBox<String> statusFilter = new ComboBox<>("Статус");
        private ComboBox<String> directionFilter = new ComboBox<>("Направление");
        private CheckBox showAllFilter = new CheckBox("Показать все сообщения", false);

        public Manage() {
            setCaption("Фильтр");
            addStyleName("colored_header");

            VerticalLayout roothz = new VerticalLayout();
            roothz.setSizeUndefined();
//            HorizontalLayout hz = new HorizontalLayout();
//            FormLayout v1 = new FormLayout();
//            v1.setMargin(false);
//            UUIDFilter.setWidth(Integer.toString(UUID_FIELD_LENGHT));
//            requestUUIDFilter.setWidth(Integer.toString(UUID_FIELD_LENGHT));
//            v1.addComponent(UUIDFilter);
//            v1.addComponent(requestUUIDFilter);
//
//            hz.addComponents(v1, v2, v3, v4, v5);
//            VerticalLayout vl = new VerticalLayout();
//            vl.addComponents(hz, buttonsLayouted);
//            vl.addStyleName("padding10px");
//            roothz.addComponents(vl);
            roothz.setMargin(false);
            this.setContent(roothz);
        }
    }

    public class Filter extends Panel {

        private TextField UUIDFilter = new TextField("UUID / Рассылка");
        private TextField requestUUIDFilter = new TextField("UUID ответа");


        public Filter() {
            setCaption("Фильтр");
            addStyleName("colored_header");

            VerticalLayout roothz = new VerticalLayout();
            roothz.setSizeUndefined();
            HorizontalLayout hz = new HorizontalLayout();
            FormLayout v1 = new FormLayout();
            v1.setMargin(false);
            UUIDFilter.setWidth("400");
            requestUUIDFilter.setWidth("400");
            v1.addComponent(UUIDFilter);
            v1.addComponent(requestUUIDFilter);

            HorizontalLayout buttonsLayouted = getButtons();
            hz.addComponents(v1);
            VerticalLayout vl = new VerticalLayout();
            vl.addComponents(hz, buttonsLayouted);
            vl.addStyleName("padding10px");
            roothz.addComponents(vl);
            roothz.setMargin(false);
            this.setContent(roothz);
        }

        private HorizontalLayout getButtons() {
            Button clear = new Button("найти");
            Button find = new Button("Индексировать");
            Button update = new Button("Созд Индекс");

            // переделать
            find.addClickListener(click -> {
                int begin = 0;
                int end = 100; //500
                List<User> data = service.getAll(begin, end);
                data.forEach(dao::addUser);
                dao.addUser(data.get(0));
                dao.dropCount();
//                while (end <= 100) { //100000
//                    List<User> data = service.getAll(begin, end);
//                    data.forEach(dao::addUser);
//                    begin = end;
//                    end = end + 500;
//                }
            });

            clear.addClickListener(click -> {
                dao.search(UUIDFilter.getValue());
                table.getDataProvider().refreshAll();
            });
            update.addClickListener(clickEvent -> dao.newPiplene());
            HorizontalLayout root = new HorizontalLayout();
            root.addComponents(clear, find, update);

            return root;
        }
    }
}
