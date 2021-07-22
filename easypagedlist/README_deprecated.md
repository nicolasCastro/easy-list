# easy-paged-list

(DEPRECATED)
> This version uses kotlin synthetics.
> Look for the [new version](README.md) to have viewBinding support.

Android Multi DataSource Paging Library in few steps. Using LiveData, AndroidX Paging2 and ViewModel.

Easy way to use paging android library with any datasource (Retrofit, Room, Realm, Json, static data, etc..) in a few steps.

## How to use easypagedlist

Add the library dependency:

     implementation 'com.github.thinkupsoft.tkup-easy-list:easypagedlist:0.1.6'

### DataSource

Create your own **DataSource** class extending from **RendererDataSource**:

    class MyDataSource(
        // ... yours params ... //
        inputSource: MutableList<RendererItem<*>> = mutableListOf(), // optional: If you wont restart a previous instance data
        firstInstance: Boolean = true // optional: If you wont use the getReinitial instance of getInitial to first load
    ) : RendererDataSource<MyDataSource>(inputSource, firstInstance)

***inputSource:*** (*optional)* If you want restart a previous instance data

***firstInstance:*** (*optional*) If you want use the getReinitial instance of getInitial to first load

Implements ***RendererDataSource*** methods:

***getKeys:*** Use to manage next and previous page keys

***getInitial:*** How to obtain the first page data

***getReinitial:*** (*optional*) How to obtain the first page data if is a firstInstance = false

***getAfter:*** How to obtain next pages data

***getBefore:*** How to obtain previous pages data

***create:*** How to obtain a new instance when invalidate data and restart

### Attaching a RecyclerView

In your Activity/Fragment use a **PagedViewModel** class instance to get an android *ViewModel*  to be a paging manager. We have a generic *PagedListAdapter* encapsulated in that *ViewModel* :

Attach a recyclerView to the custom library adatper:

    pagedViewModel.attachToRecyclerView(myRecyclerView)

the library provide default footers for loading and errors, but you can override this call:

      pagedViewModel.setFooterLayout(R.layout.my_footer_loading, R.layout.my_footer_error)

You can also use a SwipeRefreshLayout by calling the following method

    pagedViewModel.refresh()

to invalidate data and restart the pages.

### Initialization

You need to invoke  ***initList*** and ***initState***  methods from *PagedViewModel* to be able to submit the list and set footer state to the adapter, respectively. Also if you want to add a custom action, you can pass them as a block.


## Add Items

***RendererPagedAdapter*** use the Renderers pattern. To add a new Renderers you need to implement a class extending from *ViewRenderer<MyModel, View>* and pass a new instance to *pagedViewModel.addRenderers(MyRenderer())*

***NOTE:*** Add a renderers for each type of items you may have.
