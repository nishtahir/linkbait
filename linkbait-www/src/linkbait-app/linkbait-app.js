Polymer({

    is: 'linkbait-app',

    properties: {

        page: {
            type: String,
            reflectToAttribute: true,
            observer: '_pageChanged'
        },

    },

    observers: [
        '_routePageChanged(routeData.page)'
    ],

    _routePageChanged: function(page) {
        this.page = page || 'view1';
    },

    _pageChanged: function(page) {
        // load page import on demand.
        this.importHref(
            this.resolveUrl('../' + page + '-view/' + page + '-view.html'), null, null, true);
    }

});