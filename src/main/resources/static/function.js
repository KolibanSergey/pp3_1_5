getAllUsers()

function getAllUsers() {
    $('#table').empty()
    $.getJSON('/api/admin', function (data) {
        $.each(data, function (key, user) {
            let roles = []
            for (let i = 0; i < user.roles.length; i++) {
                roles.push(user.roles[i].role.substring(5))
            }
            $('#tableAllUsers').append($('<tr>').append(
                    $('<td>').text(user.id),
                    $('<td>').text(user.name),
                    $('<td>').text(user.lastName),
                    $('<td>').text(user.age),
                    $('<td>').text(user.username),
                    $('<td>').text(roles.join(', ')),
                    $('<td>').append(
                        '<button type=\'button\' data-toggle=\'modal\' class=\'btn-info btn\'' +
                        ' data-target=\'#editUserModal\' data-user-id=\'' + user.id +
                        '\'>Edit</button>'),
                    $('<td>').append(
                        '<button type=\'button\' data-toggle=\'modal\' class=\'btn btn-danger\'' +
                        ' data-target=\'#deleteUserModal\' data-user-id=\'' + user.id +
                        '\'>Delete</button>'),
                ),
            )
        })
    })
    $('[href="#admin"]').on('show.bs.tab', (e) => {
        location.reload()
    })
}


$('[href="#userss"]').on('show.bs.tab', (e) => {
    $('#change-tabContent').hide(),
        getCurrent()
})

function getCurrent() {
    $.ajax({
        url: '/getUser',
        type: 'GET',
        dataType: 'json',
    }).done((msg) => {
        let user = JSON.parse(JSON.stringify(msg))
        let roles = []
        for (let i = 0; i < user.roles.length; i++) {
            roles.push(user.roles[i].role.substring(5))
        }
        $('#current-user-table tbody').empty().append(
            $('<tr>').append(
                $('<td>').text(user.id),
                $('<td>').text(user.name),
                $('<td>').text(user.lastName),
                $('<td>').text(user.age),
                $('<td>').text(user.username),
                $('<td>').text(roles.join(', ')),
            ))
    }).fail(() => {
        alert('Error Get All Users!')
    })
}

//Edit
$('#editUserModal').on('show.bs.modal', (e) => {
    let userId = $(e.relatedTarget).data('user-id')

    $.ajax({
        url: '/api/admin/' + userId,
        type: 'GET',
        dataType: 'json',
    }).done((msg) => {
        let user = JSON.parse(JSON.stringify(msg))
        $.getJSON('api/admin/roles', function (editRole) {
            $('#id').empty().val(user.id)
            $('#editName').empty().val(user.name)
            $('#editLastName').empty().val(user.lastName)
            $('#editAge').empty().val(user.age)
            $('#editUserName').empty().val(user.username)
            $('#editPassword').empty().val(user.password)
            $('#editRoles').empty()
            $.each(editRole, (i, role) => {
                $('#editRoles').append(
                    $('<option>').text(role.role.substring(5)),
                )
            })
            $('#editRoles').val(user.roles)
        })
    })
})

$('#buttonEditSubmit').on('click', (e) => {
    e.preventDefault()

    let editUser = {
        id: $('#id').val(),
        name: $('#editName').val(),
        lastName: $('#editLastName').val(),
        age: $('#editAge').val(),
        userName: $('#editUserName').val(),
        password: $('#editPassword').val(),
        roles: $('#editRoles').val(),
    }

    $.ajax({
        url: '/api/admin',
        type: 'PUT',
        contentType: 'application/json; charset=utf-8',
        dataType: 'json',
        data: JSON.stringify(editUser),
        success: function (response) {


            console.log(response);
        },
    })
    $('#editUserModal').modal('hide')
    location.reload()
    getAllUsers()
})
//Delete
$('#deleteUserModal').on('show.bs.modal', (e) => {
    let userId = $(e.relatedTarget).data('user-id')

    $.ajax({
        url: '/api/admin/' + userId,
        type: 'GET',
        dataType: 'json',
    }).done((msg) => {
        let user = JSON.parse(JSON.stringify(msg))

        $('#delId').empty().val(user.id)
        $('#delName').empty().val(user.name)
        $('#delLastName').empty().val(user.lastName)
        $('#delAge').empty().val(user.age)
        $('#delUserName').empty().val(user.username)

        $('#buttonDel').on('click', (e) => {
            e.preventDefault()

            $.ajax({
                url: '/api/admin/' + userId,
                type: 'DELETE',
                dataType: 'text',
            }).done((deleteMsg) => {
                $(`#deleteUserModal`).modal('hide')
                location.reload()
            })
        })
    })
})

$('[href="#new"]').on('show.bs.tab', (e) => {
    $.getJSON('api/admin/roles', function (newUser) {
        $(() => {
            $('#name').empty().val('')
            $('#lastName').empty().val('')
            $('#age').empty().val('')
            $('#userName').empty().val('')
            $('#password').empty().val('')
            $('#rolesNew').empty().val('')
            $.each(newUser, function (k, role) {
                $('#rolesNew').append(
                    $('<option>').text(role.role.substring(5)),
                )
            })
        })
    })
})
$('#buttonNew').on('click', (e) => {
    e.preventDefault()

    let newUser = {
        name: $('#name').val(),
        lastName: $('#lastName').val(),
        age: $('#age').val(),
        userName: $('#userName').val(),
        password: $('#password').val(),
        roles: $('#rolesNew').val(),
    }
    let json = JSON.stringify(newUser);
    console.log(json);


    $.ajax({
        url: '/api/admin',
        type: 'POST',
        contentType: 'application/json; charset=utf-8',
        dataType: 'json',
        data: json,
        success: function (response) {

            console.log(response);
        },
        error: function (jqXHR) {

            if (jqXHR.status >= 400) {

                let errorMessage = jqXHR.responseJSON.errorMessage;

                console.log(errorMessage);
                let errorMessage1 = jqXHR.responseJSON.info;
                console.log(errorMessage1);

                $("div[role='alert']")
                    .addClass("alert alert-danger")
                    .text(errorMessage);
                $("div[role='alert']")
                    .addClass("alert alert-danger")
                    .text(errorMessage1);
            }

        }

    }).done(function () {
        $('#admin-tab').click()
        $('#tableAllUsers').empty()
        getAllUsers()
    })

})