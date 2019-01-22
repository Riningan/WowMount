# WowMount


![logo](./app/src/main/res/drawable-mdpi/drawable_logo.png)


Application architecture
---

**MVP (Moxy) Single activity application** with kodein injections.
Data from repository send to presenters via interactor through **Rx**.

Used libraries:

* [Realm](https://realm.io/)
* [Moxy](https://github.com/Arello-Mobile/Moxy)
* [Kodein](https://kodein.org/di)
* [Retrofit](https://github.com/square/retrofit)
* [RxJava2](https://github.com/ReactiveX/RxJava)
* [Cicerone](https://github.com/terrakok/Cicerone)
* [Frarg](https://github.com/Riningan/Frarg)
* [LeakCanary](https://github.com/square/leakcanary)


Used libraries for test:

* [Mockito](https://github.com/mockito/mockito)
* [PowerMock](https://github.com/powermock/powermock)
* [MockK](https://mockk.io/)
* [Awaitility](https://github.com/awaitility/awaitility)
* [Espresso](https://developer.android.com/training/testing/espresso)


Code style conventions 
---

### General

[Naming](https://kotlinlang.org/docs/reference/coding-conventions.html#naming-rules)
[Formatting](https://kotlinlang.org/docs/reference/coding-conventions.html#formatting)


### UI fragment naming

Fragment naming **(Case-sensitive)**:
* `package` ui.**<name under\_score без \_>** 
* `fragment class` **<Name UpperCamelCase>** Fragment.kt
* `view interface` **<Name UpperCamelCase>** View.kt
* `presenter class` **<Name UpperCamelCase>** Presenter.kt
* `res/layout` fraqment\_**<name under_score>**.xml
* `res/menu` menu\_**<name under_score>**.xml


### Class organization

* public fields
* package-private fields
* protected fields
* private fields
* properties
* constructors
* override class methods
* override interface methods
* inline methods
* public methods
* package-private methods
* protected methods
* private methods
* abstract methods
* enums
* inner interface
* inner class
* companion objects


### Views naming

android:id="@+id/***<type>*<Fragment name UpperCamelCase>***<additional information UpperCamelCase>*".

Ignoring AppCompat in view.

Types: 
* LinearLayout - ll
* FrameLayout - fl
* RelativeLayout - rl
* CoordinatorLayout - crdl
* ConstraintLayout - cnsl
* TextView - tv
* ImageView - iv
* RecyclerView - rv
* TabLayout - tl
* ViewPager - vp
* CheckBox - cb
* Switch - swt
* ToolBar - tlb
* Button - btn
* ImageButton - ibtn
* ImageSwitcher - iswt
* FloatingActionButton - fabtn
* NestedScrollView - nsv
* ScrollView - sv
* BottomNavigationView - bnv
* View - view
* ProgressBar - pb
* WebView - wv


### Layout type naming

* `Activity` - activity\_**<name under\_score>**
* `Fragment как окно приложения` - fragment\_**<name>**
* `Fragment для ViewPager` - page\_**<parent fragment name>**\_**<additional information>**
* `Fragment для диалога` - dialog\_**<parent fragment name>**\_**<additional information>**
* `Include` - layout\_**<parent fragment name>**\_**<additional information>**
* `ViewHolder` - item\_**<parent fragment name>**\_**<additional information>**
* `View` - view\_**<additional information>**

In case there layout using in several place, **name** must replace by **all**. Also in this case should be renamed all ids. 


LICENCE
-----

  	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at
	
	   http://www.apache.org/licenses/LICENSE-2.0
	
	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.