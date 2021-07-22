# easy-list
Using a single adapter for all yours Recyclerviews.

[![](https://jitpack.io/v/thinkupsoft/tkup-easy-list.svg)](https://jitpack.io/#thinkupsoft/tkup-easy-list)

Easy way to use an Recyclerview adapter for any kind of items.

## How to use easylist

Add the library dependency:

     implementation 'com.github.thinkupsoft.tkup-easy-list:easylist:{lastVersion}'

### Create an instance

```
val adapter = RendererAdapter()
```

and populate it to the recyclerView

```
binding.recyclerView.adapter = adapter
```

## Add Items

***RendererAdapter*** use the Renderers pattern.
To add a new Renderers you need to implement a class extending from *ViewRenderer<MyModel, ViewBinding>*
and pass a new instance to *adapter.addRenderer(MyRenderer())*

***NOTE:*** Add a renderers for each type of items you may have.
