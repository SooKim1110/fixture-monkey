---
title: "InnerSpec"
weight: 40
menu:
docs:
parent: "customizing-objects"
identifier: "innerspec"
---

An InnerSpec is a type-independent specification for the customizations to be applied.
With `setInner()` method of ArbitraryBuilder, you can apply customizations defined within an InnerSpec instance to your builder.

You can make an InnerSpec instance that has the information about the customizations you want to make, and reuse it on arbitraryBuilders.
Unlike using expressions when using customization methods in ArbitraryBuilder, InnerSpec defines customizations in a more nested manner.
It also gives some ways to customize map properties which is not possible with normal expressions.


{{< alert icon="💡" text="kotlin EXP is not supported for innerSpec since it is intended to be type-independent. You have to specify the property by its name" />}}

# Applying InnerSpec on the ArbitraryBuilder

You can use your predefined InnerSpec on your builder using the `setInner()` method like this:

{{< tabpane persist=false >}}
{{< tab header="Java" lang="java">}}

InnerSpec innerSpec = new InnerSpec().property("id", 1000);

fixtureMonkey.giveMeBuilder(Product.class)
    .setInner(innerSpec);

{{< /tab >}}
{{< tab header="Kotlin" lang="kotlin">}}

val innerSpec = InnerSpec().property("id", 1000)

fixtureMonkey.giveMeBuilder<Product>()
    .setInner(innerSpec)

{{< /tab >}}
{{< /tabpane>}}

# Customizing properties

## property()

Similar to the `set()` method in ArbitraryBuilder you can specify the property you'd like to customize by writing the property name.

{{< alert icon="🚨" text="Fixture Monkey expressions such as refering elements (`[]`) or nested fields(`.`) are not allowed as the property name. Only the property name itself is allowed." />}}

{{< tabpane persist=false >}}
{{< tab header="Java" lang="java">}}

InnerSpec innerSpec = new InnerSpec()
    .property("id", 1000);

{{< /tab >}}
{{< tab header="Kotlin" lang="kotlin">}}

val innerSpec = InnerSpec()
    .property("id", 1000)

{{< /tab >}}
{{< /tabpane>}}

## size(), minSize(), maxSize()

`size()`, `minSize()`, and `maxSize()` can be used to specify the size of the property.

As mentioned above, InnerSpec specifies the customization to be made in a nested manner.
From the example we can see that we first have to select the property with `property()` with the name("options") and then write an innerSpec consumer to specify the size.

{{< tabpane persist=false >}}
{{< tab header="Java" lang="java">}}

InnerSpec innerSpec = new InnerSpec()
    .property("options", options -> options.size(5)); // size:5

InnerSpec innerSpec = new InnerSpec()
    .property("options", options -> options.size(3, 5)); // minSize:3, maxSize:5

InnerSpec innerSpec = new InnerSpec()
    .property("options", options -> options.minSize(3)); // minSize:3

InnerSpec innerSpec = new InnerSpec()
    .property("options", options -> options.maxSize(5)); // maxSize:5

{{< /tab >}}
{{< tab header="Kotlin" lang="kotlin">}}

val innerSpec = InnerSpec()
    .property("options") { it.size(5) } // size:5

val innerSpec = InnerSpec()
    .property("options") { it.size(3, 5) } // minSize:3, maxSize:5

val innerSpec = InnerSpec()
    .property("options") { it.minSize(3) } // minSize:3

val innerSpec = InnerSpec()
    .property("options") { it.maxSize(5) } // maxSize:5

{{< /tab >}}
{{< /tabpane>}}

## postCondition()

`postCondition()` can be used when you need to specify that your property has to match a specific condition.

{{< alert icon="🚨" text="Using setPostCondition can incur higher costs for narrow conditions. In such cases, it's recommended to use set instead." />}}

{{< tabpane persist=false >}}
{{< tab header="Java" lang="java">}}

InnerSpec innerSpec = new InnerSpec()
    .property("id", id -> id.postCondition(Long.class, it -> it > 0));

{{< /tab >}}
{{< tab header="Kotlin" lang="kotlin">}}

val innerSpec = InnerSpec()
    .property("id") { it.postCondition(Long::class.java) { it > 0 }}

{{< /tab >}}
{{< /tabpane>}}

## inner()

{{< tabpane persist=false >}}
{{< tab header="Java" lang="java">}}

InnerSpec innerSpec = new InnerSpec()
.property("id", 1000);

{{< /tab >}}
{{< tab header="Kotlin" lang="kotlin">}}

val innerSpec = InnerSpec()
.property("id", 1000)

{{< /tab >}}
{{< /tabpane>}}

# Customizing list properties

## listElement()

## allListElement()

# Customizing map properties

{{< alert icon="🚨" text="Just like lists, setting a map without size might result in no change. Please check out if the map property has the size you are willing to set before setting value." />}}

## key(), value(), entry()

## keys(), values(), entries()

## keyLazy(), valueLazy(), entryLazy()

## allKey(), allValue(), allEntry()

## keyLazy(), allValueLazy(), allEntryLazy()

# Customizing Nested Maps


