package net.almer.coloristic.client.screen;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ibm.icu.text.Transliterator;
import com.mojang.serialization.JsonOps;
import net.almer.coloristic.Coloristic;
import net.almer.coloristic.client.ColoristicFx;
import net.almer.coloristic.client.presets.Preset;
import net.almer.coloristic.client.presets.PresetListWidget;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.ParentElement;
import net.minecraft.client.gui.navigation.GuiNavigationPath;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.pack.PackListWidget;
import net.minecraft.client.gui.screen.pack.ResourcePackOrganizer;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.*;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.util.ScreenshotRecorder;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.Util;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.FileAttribute;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Environment(EnvType.CLIENT)
public class PresetScreen extends Screen {
    private static final Text TITLE_TEXT = Text.translatable("option.preset_title");
    private static final Text AVAILABLE_TITLE = Text.translatable("pack.available.title");
    private static final Text OPEN_FOLDER = Text.translatable("pack.openFolder");
    private static final Text FOLDER_INFO;
    private final Path file;
    private ButtonWidget doneButton;
    private TextFieldWidget enterNameField;
    private ButtonWidget confirmButton;
    private ButtonWidget createButton;
    private ButtonWidget undoButton;
    private PresetListWidget availablePackList;
    public ButtonWidget useButton;
    @Nullable
    private DirectoryWatcher directoryWatcher;
    private final ThreePartsLayoutWidget layout = new ThreePartsLayoutWidget(this);
    private long refreshTimeout;
    private final Map<String, Identifier> iconTextures = Maps.newHashMap();
    protected PresetScreen(Path file) {
        super(TITLE_TEXT);
        this.file = file;
        this.directoryWatcher = DirectoryWatcher.create(file);
    }
    private void closeDirectoryWatcher() {
        if (this.directoryWatcher != null) {
            try {
                this.directoryWatcher.close();
                this.directoryWatcher = null;
            } catch (Exception var2) {
            }
        }
    }
    @Override
    protected void init() {
        DirectionalLayoutWidget directionalLayoutWidget = (DirectionalLayoutWidget)this.layout.addHeader(DirectionalLayoutWidget.vertical().spacing(5));
        directionalLayoutWidget.getMainPositioner().alignHorizontalCenter();
        this.availablePackList = (PresetListWidget)this.addDrawableChild(new PresetListWidget(this.client, this, 200, this.height - 66, AVAILABLE_TITLE));
        directionalLayoutWidget.add(new TextWidget(this.getTitle(), this.textRenderer));
        DirectionalLayoutWidget directionalLayoutWidget2 = (DirectionalLayoutWidget)this.layout.addFooter(DirectionalLayoutWidget.horizontal().spacing(8));
        DirectionalLayoutWidget directionalLayoutWidget1 = this.layout.addBody(DirectionalLayoutWidget.horizontal().spacing(8));
        directionalLayoutWidget1.getMainPositioner().margin(200, -57, 0, 0);
        this.useButton = directionalLayoutWidget1.add(ButtonWidget.builder(Text.translatable("option.use_preset"), (button) -> {
            this.availablePackList.getSelectedOrNull().toggle();
        }).width(200).build());
        directionalLayoutWidget2.add(ButtonWidget.builder(OPEN_FOLDER, (button) -> {
            Util.getOperatingSystem().open(this.file);
        }).tooltip(Tooltip.of(FOLDER_INFO)).build());
        this.doneButton = (ButtonWidget)directionalLayoutWidget2.add(ButtonWidget.builder(ScreenTexts.DONE, (button) -> {
            this.client.setScreen(new ColorSettingsScreen());
        }).build());
        DirectionalLayoutWidget directionalLayoutWidget3 = this.layout.addBody(DirectionalLayoutWidget.vertical().spacing(6));
        directionalLayoutWidget3.getMainPositioner().margin(200, 0, 0, 0);
        this.enterNameField = directionalLayoutWidget3.add(new TextFieldWidget(this.client.textRenderer, 200, 20, Text.translatable("text.defaultName").append("1")));
        this.confirmButton = directionalLayoutWidget3.add(ButtonWidget.builder(Text.translatable("option.confirm"), (button) -> {
            Preset preset = new Preset(this.enterNameField.getText(), List.of((double)ColoristicFx.INSTANCE.redMatrix1, (double)ColoristicFx.INSTANCE.redMatrix2, (double)ColoristicFx.INSTANCE.redMatrix3,
                    (double)ColoristicFx.INSTANCE.greenMatrix1, (double)ColoristicFx.INSTANCE.greenMatrix2, (double)ColoristicFx.INSTANCE.greenMatrix3,
                    (double)ColoristicFx.INSTANCE.blueMatrix1, (double)ColoristicFx.INSTANCE.blueMatrix2, (double)ColoristicFx.INSTANCE.blueMatrix3,
                    (double)ColoristicFx.INSTANCE.colorScale1, (double)ColoristicFx.INSTANCE.colorScale2, (double)ColoristicFx.INSTANCE.colorScale3,
                    (double)ColoristicFx.INSTANCE.saturation, (double)ColoristicFx.INSTANCE.resolution, (double)ColoristicFx.INSTANCE.mosaicSize,
                    (double)ColoristicFx.INSTANCE.inverseAmount, (double)ColoristicFx.INSTANCE.radius));
            this.writePreset(preset);
            this.enterNameField.active = false;
            this.confirmButton.active = false;
            this.undoButton.active = false;
            this.createButton.active = true;
            this.updatePackLists();
        }).width(174).build(), directionalLayoutWidget3.copyPositioner().margin(226, 0, 0, 0));
        this.undoButton = directionalLayoutWidget3.add(ButtonWidget.builder(Text.literal("x"), (button) ->{
            this.enterNameField.active = false;
            this.confirmButton.active = false;
            this.undoButton.active = false;
            this.createButton.active = true;
        }).width(20).build(), directionalLayoutWidget3.copyPositioner().margin(200, -26, 0, 0));
        this.enterNameField.active = false;
        this.confirmButton.active = false;
        this.undoButton.active = false;
        this.createButton = directionalLayoutWidget2.add(ButtonWidget.builder(Text.translatable("option.create_preset"), (button) ->{
            this.enterNameField.active = true;
            this.confirmButton.active = true;
            this.undoButton.active = true;
            this.createButton.active = false;
        }).width(100).build());
        this.refresh();
        this.layout.forEachChild((element) -> {
            ClickableWidget var10000 = (ClickableWidget)this.addDrawableChild(element);
        });
        this.initTabNavigation();
    }
    protected void initTabNavigation() {
        this.layout.refreshPositions();
        this.availablePackList.position(200, this.layout);
        this.availablePackList.setX(this.width / 2 - 15 - 200);
    }
    private void updatePackLists() {
        String path = Path.of("./coloristic_presets/").toAbsolutePath().normalize().toString();
        new File("coloristic_presets").mkdir();
        try(Stream<Path> paths = Files.list(Path.of(path))){
            List<Preset> presets = new ArrayList<>();
            paths.forEach((file) ->{
                if(file.toString().endsWith(".json")) {
                    try (FileReader reader = new FileReader(file.toFile())) {
                        Preset preset = new Gson().fromJson(reader, Preset.class);
                        presets.addLast(preset);
                    } catch (FileNotFoundException var) {
                        Coloristic.LOGGER.warn("Can not read directory");
                    } catch (IOException var1) {
                        Coloristic.LOGGER.warn("Can not read directory");
                    }
                }
            });
            this.updatePackList(this.availablePackList, presets.stream());
        } catch(IOException var){
            Coloristic.LOGGER.warn("Can not read directory");
        }
    }
    private void updatePackList(PresetListWidget widget, Stream<Preset> presets) {
        widget.children().clear();
        PresetListWidget.PresetEntry presetsEntry = (PresetListWidget.PresetEntry)widget.getSelectedOrNull();
        String string = presetsEntry == null ? "" : presetsEntry.getName();
        widget.setSelected(null);
        presets.forEach((preset) -> {
            PresetListWidget.PresetEntry presetEntry = new PresetListWidget.PresetEntry(this.client, widget, preset);
            widget.children().add(presetEntry);
            if (preset.getName().equals(string)) {
                widget.setSelected(presetEntry);
            }
        });
    }
    public void switchFocusedList(PresetListWidget listWidget) {
        PresetListWidget packListWidget = this.availablePackList;
        this.switchFocus(GuiNavigationPath.of(packListWidget.getFirst(), new ParentElement[]{packListWidget, this}));
    }
    public void tick() {
        if (this.directoryWatcher != null) {
            try {
                if (this.directoryWatcher.pollForChange()) {
                    this.refreshTimeout = 20L;
                }
            } catch (IOException var2) {
                Coloristic.LOGGER.warn("Failed to poll for directory {} changes, stopping", this.file);
                this.closeDirectoryWatcher();
            }
        }
        if (this.refreshTimeout > 0L && --this.refreshTimeout == 0L) {
            this.refresh();
        }
        if(this.availablePackList.getSelectedOrNull() != null){
            this.useButton.active = true;
        }
        else{
            this.useButton.active = false;
        }
    }
    public static void writePreset(Preset preset){
        String[] nameArr = preset.getName().toLowerCase().split(" ");
        String name = "";
        for(int i = 0; i < nameArr.length; i++){
            name += nameArr[i];
            if(i < nameArr.length - 1){
                name += "_";
            }
        }
        String path = Path.of("./coloristic_presets/", name + ".json", "").toAbsolutePath().normalize().toString();
        String path1 = Path.of("./coloristic_presets/", name + ".png", "").toAbsolutePath().normalize().toString();
        new File("coloristic_presets").mkdir();
        try (FileWriter writer = new FileWriter(path)){
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            JsonObject obj = preset.CODEC.encodeStart(JsonOps.INSTANCE, preset).getOrThrow().getAsJsonObject();
            gson.toJson(obj, gson.newJsonWriter(writer));

            MinecraftClient client1 = MinecraftClient.getInstance();
            NativeImage nativeImage = ScreenshotRecorder.takeScreenshot(client1.getFramebuffer());
            Util.getIoWorkerExecutor().execute(() -> {
                int i = nativeImage.getWidth();
                int j = nativeImage.getHeight();
                int k = 0;
                int l = 0;
                if (i > j) {
                    k = (i - j) / 2;
                    i = j;
                } else {
                    l = (j - i) / 2;
                    j = i;
                }
                try {
                    NativeImage nativeImage2 = new NativeImage(64, 64, false);
                    try {
                        nativeImage.resizeSubRectTo(k, l, i, j, nativeImage2);
                        nativeImage2.writeTo(Path.of(path1));
                    } catch (Throwable var15) {
                        try {
                            nativeImage2.close();
                        } catch (Throwable var14) {
                            var15.addSuppressed(var14);
                        }
                        throw var15;
                    }
                    nativeImage2.close();
                } catch (IOException var16) {
                    Coloristic.LOGGER.warn("Couldn't save auto screenshot", var16);
                } finally {
                    nativeImage.close();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void refresh() {
        this.updatePackLists();
        this.refreshTimeout = 0L;
        this.iconTextures.clear();
    }
    static {
        FOLDER_INFO = Text.translatable("pack.folderInfo");
    }
    @Environment(EnvType.CLIENT)
    private static class DirectoryWatcher implements AutoCloseable {
        private final WatchService watchService;
        private final Path path;

        public DirectoryWatcher(Path path) throws IOException {
            this.path = path;
            this.watchService = path.getFileSystem().newWatchService();

            try {
                this.watchDirectory(path);
                DirectoryStream<Path> directoryStream = Files.newDirectoryStream(path);

                try {
                    Iterator var3 = directoryStream.iterator();

                    while(var3.hasNext()) {
                        Path path2 = (Path)var3.next();
                        if (Files.isDirectory(path2, new LinkOption[]{LinkOption.NOFOLLOW_LINKS})) {
                            this.watchDirectory(path2);
                        }
                    }
                } catch (Throwable var6) {
                    if (directoryStream != null) {
                        try {
                            directoryStream.close();
                        } catch (Throwable var5) {
                            var6.addSuppressed(var5);
                        }
                    }

                    throw var6;
                }

                if (directoryStream != null) {
                    directoryStream.close();
                }

            } catch (Exception var7) {
                this.watchService.close();
                throw var7;
            }
        }

        @Nullable
        public static PresetScreen.DirectoryWatcher create(Path path) {
            try {
                return new PresetScreen.DirectoryWatcher(path);
            } catch (IOException var2) {
                Coloristic.LOGGER.warn("Failed to initialize preset directory {} monitoring", path, var2);
                return null;
            }
        }

        private void watchDirectory(Path path) throws IOException {
            path.register(this.watchService, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE, StandardWatchEventKinds.ENTRY_MODIFY);
        }

        public boolean pollForChange() throws IOException {
            boolean bl = false;

            WatchKey watchKey;
            while((watchKey = this.watchService.poll()) != null) {
                List<WatchEvent<?>> list = watchKey.pollEvents();
                Iterator var4 = list.iterator();

                while(var4.hasNext()) {
                    WatchEvent<?> watchEvent = (WatchEvent)var4.next();
                    bl = true;
                    if (watchKey.watchable() == this.path && watchEvent.kind() == StandardWatchEventKinds.ENTRY_CREATE) {
                        Path path = this.path.resolve((Path)watchEvent.context());
                        if (Files.isDirectory(path, new LinkOption[]{LinkOption.NOFOLLOW_LINKS})) {
                            this.watchDirectory(path);
                        }
                    }
                }

                watchKey.reset();
            }

            return bl;
        }

        public void close() throws IOException {
            this.watchService.close();
        }
    }
}
