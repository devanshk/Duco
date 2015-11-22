![Duco](https://github.com/devanshk/Duco/blob/master/Designs/readme_duco_thin.png?raw=true)

Duco is Tinder for artwork, created at the Carnegie Museum of Art's 120th Anniversary.

##What it does
Duco helps users find art they like, and museums gauge what art customers look for.

We show the user an art piece, and they make a snap judgement on whether they like it. As the user keeps swiping, our algorithm determines their preference to show them more artwork they like and less they don't.

Here's an example of an architecture-buff swiping around.

<p align="center">
  <img src="https://github.com/devanshk/Duco/blob/master/Designs/rec_1_convert_loop.gif?raw=true"/>
</p>

You'll notice the artwork focuses towards buildings as they keep swiping right. In order to keep the user exposed to a diverse collection, every fifth item is a wildcard chosen for its lower rating.

If a user isn't sure whether or not they like an art piece, they have easy access to more info.
<p align="center">
  <img src="https://github.com/devanshk/Duco/blob/master/Designs/rec_2_trim.gif?raw=true"/>
</p>

And if they want to understand their own preferences, the algorithm can show them what it's determined.
<p align="center">
  <img src="https://github.com/devanshk/Duco/blob/master/Designs/rec_3_trim.gif?raw=true"/>
</p>
