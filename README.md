# کامیار — پخش‌کننده موسیقی اندروید

یک موزیک پلیر ساده که آهنگ‌های موجود روی حافظه گوشی را پیدا می‌کند و با ظاهری
شبیه بقیه پخش‌کننده‌ها آن‌ها را پخش می‌کند.

## قابلیت‌ها

- خواندن کتابخانه موسیقی گوشی از طریق MediaStore (بدون نیاز به انتخاب دستی پوشه)
- فهرست آهنگ‌ها با نام خواننده و مدت زمان، جستجو بر اساس نام/خواننده/آلبوم
- پخش‌کننده کامل: پخش/توقف، بعدی/قبلی، نوار پیشرفت با قابلیت جابجایی
- پخش در پس‌زمینه با نوتیفیکیشن (MediaSession + ExoPlayer)، ادامه پخش بعد از بستن اپ
- حالت تصادفی، تکرار همه/یک آهنگ، تغییر سرعت پخش (۰٫۷۵x تا ۲x)
- مینی‌پلیر چسبیده به پایین صفحه در حین مرور لیست
- رابط کاربری فارسی و راست‌به‌چپ، تم روشن/تاریک (Material 3، رنگ‌های داینامیک اندروید ۱۲+)

## بیلد گرفتن

### راه ۱: بدون نصب هیچ ابزاری (GitHub Actions)

فقط مخزن را روی GitHub پوش کنید؛ فایل `.github/workflows/build-apk.yml` خودش
APK را می‌سازد و در بخش **Releases** (و همچنین Artifacts) قرار می‌دهد.

### راه ۲: روی سیستم خودتان

نیاز به JDK 17 و Android SDK:

```bash
./gradlew assembleDebug
```

خروجی: `app/build/outputs/apk/debug/app-debug.apk`

## نصب روی گوشی

1. فایل APK را به گوشی منتقل کنید.
2. از تنظیمات، نصب از منابع نامعتبر (Install unknown apps) را برای فایل‌منیجر
   یا مرورگرتان فعال کنید.
3. روی فایل بزنید و نصب کنید.
4. در اولین اجرا اجازه دسترسی به «فایل‌های صوتی / موسیقی و صدا» را بدهید.

## ساختار کد

```
app/src/main/java/com/kamyar/player/
├── MainActivity.kt            # نقطه ورود، فقط Composable را سوار می‌کند
├── data/Song.kt                # مدل Song + خواندن MediaStore
├── playback/MusicService.kt    # MediaSessionService + ExoPlayer + PlaybackHolder
├── playback/PlayerViewModel.kt # اتصال به MediaController و وضعیت برای UI
├── ui/KamyarApp.kt             # مسیریابی ساده + صفحه اجازه دسترسی
├── ui/LibraryScreen.kt         # لیست آهنگ‌ها + مینی‌پلیر
├── ui/NowPlayingScreen.kt      # صفحه تمام‌صفحه پخش
├── ui/theme/Theme.kt           # رنگ‌ها و تم Material 3
└── util/Format.kt              # قالب‌بندی زمان (تست‌شده در unit test)
```

آیکون‌ها با اسکریپت `tools/make_icons.py` تولید می‌شوند (نیاز به Pillow).
