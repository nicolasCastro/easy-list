# easy-paged-list
Android Multi DataSource Paging Library in few steps. Using LiveData, AndroidX Paging, ViewModel

[![](https://jitpack.io/v/nicolasCastro/easy-paged-list.svg)](https://jitpack.io/#nicolasCastro/easy-paged-list)

Easy way to use paging android library with anything datasource (retrofit, room, realm, json, static data, etc..) in a few steps.

## How to use

Add the library dependency:

     implementation 'com.github.nicolasCastro:easy-paged-list:X.Y.Z'
     
### DataSource

Create your own **DataSource** class extending from **RendererDataSource**

    class MyDataSource(
        // ... yours params ... //
        inputSource: MutableList<RendererItem<*>> = mutableListOf(), // optional: If you wont restart a previous instance data
        firstInstance: Boolean = true // optional: If you wont use the getReinitial instance of getInitial to first load
    ) : RendererDataSource<MyDataSource>(inputSource, firstInstance)
    
***inputSource:*** *optional:* If you wont restart a previous instance data

***firstInstance:*** *optional:* If you wont use the getReinitial instance of getInitial to first load

Implements ***RendererDataSource*** methods:

***getKeys:*** Use to manage next and previous page keys
***getInitial:*** How to obtain the first page data
***getReinitial:*** *optional* How to obtain the first page data if is a firstInstance = false
***getAfter:*** How to obtain next pages data
***getBefore:*** How to obtain previous pages data
***create:*** How to obtain a new instance when invalidate data and restart

### Attaching a RecyclerView

In your Activity/Fragment use a **PagedViewModel** class instance to get an android *ViewModel*  which be a paging manager. Encapsulated in that *ViewModel* we have a generic *PagedListAdapter*:

Attach a recyclerView to the custom library adatper:

    pagedViewModel.attachToRecyclerView(myRecyclerView)

the library provide default footers to loading and errors, but you can override this calling:

      pagedViewModel.setFooterLayout(R.layout.my_footer_loading, R.layout.my_footer_error)
      
If you want uses a SwipeRefreshLayout, do it calling 

    pagedViewModel.refresh()
    
to invalidate data and restart the pages.  

### Initialization

Is necessary call from *PagedViewModel* two methods ***initList*** and ***initState*** to submit the list and set footer state to the adapter, respectively. And if you want add a custom action, can you pass them as a block.


## Add Items

***RendererPagedAdapter*** use the Renderers pattern, so, to add a new Renderers implement a class extending from *ViewRenderer<MyModel, View>* and pass an instance to *pagedViewModel.addRenderers(MyRenderer())*

***NOTE:*** Add a renderers for each type of items you have
